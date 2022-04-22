package com.odenzo.xxml

import cats._
import cats.implicits._
import org.scalajs.dom.{Attr, CDATASection, Comment, Document, DocumentType, Element, Node, ProcessingInstruction, Text}
import scala.xml.{Elem, MetaData, NamespaceBinding, NodeSeq, Null, SpecialNode, TopScope}

/** Uses ScalaJS DOM which is runnable in ScalaJS Browser and NodeJS environments to parse XML text and produce scala-xml "DOM", as a node.
  * No namespace support.
  */
object XXMLParser extends com.odenzo.xxml.XXMLParserInterface {

  private val xmlMimeType = org.scalajs.dom.MIMEType.`application/xml`

  /** This throws unexpected exceptions which are generally non-recoverable. */
  override def parse(s: String): scala.xml.Elem = {
    println("JS Parsing")
    val doc: Document = parseText(s)
    convertToScalaXML(doc)
  }

  def parseText(xmlStr: String): Document =
    new org.scalajs.dom.DOMParser().parseFromString(xmlStr, xmlMimeType)

  def convertToScalaXML(doc: Document): Elem = {
    transformToScalaXML(doc) match {
      case elem: Elem        => elem: Elem
      case xml.Group(nodes)  => throw new IllegalStateException(s"IMPOSSIBLE for Root to Be Group Node: $nodes")
      case node: SpecialNode => throw new IllegalStateException(s"IMPOSSIBLE for Root to Be Special Node: $node")
      case other             => throw new IllegalStateException("IMPOSSIBLE for Root to Be Other Node: $other")
    }
  }

  def transformToScalaXML(doc: Document): xml.Node = {
    // Throwing this away, but entity refs and at least some validation already done.
    // val docType = doc.doctype

    doc.documentElement.normalize()
    val root: Element                                = doc.documentElement
    val defaultNamespace: scala.xml.NamespaceBinding = scala.xml.TopScope
    recursiveDescent(root, defaultNamespace).value

  }

  /** Try and re-write with Eval but tail-rec enabled.
    * @param currScope
    *   A threaded list of NamespaceBindings, used as a Stack basically (via callstack), List[Namespace] would be just as good
    */
  private[odenzo] def recursiveDescent(root: org.scalajs.dom.Element, currScope: NamespaceBinding): Eval[scala.xml.Node] = {
    val myScope: NamespaceBinding = findXmlns(root, currScope).getOrElse(currScope)
    val rootsChildren: List[Node] = root.childNodes.toList // Seperated for type inference

    val (elemKids, nonElemKids) = rootsChildren.partition {
      case e: Element => true
      case n: Node    => false
    }

    val nonElemSXML                         = nonElemKids.map(convert).reduceLeft((a: NodeSeq, b: NodeSeq) => a.appendedAll(b))
    // Need to keep the kids in order between element and nodes even to be conforming.
    // So, will need to pass down state of the current list of children before it that is will append to.
    val kids: Eval[List[scala.xml.NodeSeq]] = rootsChildren
      .traverse {
        case e: Element => Eval.defer(recursiveDescent(e, myScope))
        case n: Node    => Eval.now(convert(n))
      }

    kids.map { children =>
      val compact = children.reduceLeft((a: NodeSeq, b: NodeSeq) => a.appendedAll(b))
      closeElement(root, compact)
    }
  }

  private[odenzo] def closeElement(v: Element, children: Seq[scala.xml.Node]): scala.xml.Elem = {
    scala.xml.Elem(
      prefix = v.prefix,
      label = v.localName,
      attributes = convertAttributes(v),
      scope = xml.TopScope,
      minimizeEmpty = true,
      child = children: _*
    )
  }

  private[odenzo] def convert(n: Node): scala.xml.NodeSeq = {
    n match {
      case v: Document              => scala.xml.NodeSeq.Empty               // Will never see
      case v: DocumentType          => scala.xml.NodeSeq.Empty               // Will never see since from docroot
      case v: Element               => scala.xml.NodeSeq.Empty               // Made on the closing element so ignored
      case v: Comment               => scala.xml.Comment(v.nodeValue)
      case v: CDATASection          => scala.xml.PCData(v.textContent)
      case v: Text                  => scala.xml.Text(v.textContent)
      case v: ProcessingInstruction => scala.xml.ProcInstr(v.target, v.data) // What to do with these? Could check ?xml is standalone?
    }
  }

  /** Converts scalajs.dom attributes to scala-xml attributes, leaves any xmlns:nameSpaceUri attributes in. */
  private[odenzo] def convertAttributes(elem: Element): MetaData = {
    val attrs: List[Attr]  = elem.attributes.toList.map(_._2)
    val md: List[MetaData] = attrs.map(a => scala.xml.Attribute(Option(a.prefix), a.localName, scala.xml.Text(a.value), Null))
    md match {
      case Nil   => scala.xml.Node.NoAttributes
      case multi => multi.reduceLeft((attr1, attr2) => attr1.append(attr2))
    }

  }

  /** scala-xml namespace is always empty, even on JVM, so not sure the point of this. (prefixed or not) Perhaps it will enable some
    * scala-xml sanity checks not happening on DOM parser. But since no validation I think this is all happening already.
    * @return
    *   Maybe 1 binding (with or without prefix) on the given element
    */
  private[odenzo] def findXmlns(element: Element, currNamespace: scala.xml.NamespaceBinding): Option[NamespaceBinding] = {
    element.attributes.toList
      .collectFirst {
        case (_, attr: Attr) if attr.prefix == "xmlns" => scala.xml.NamespaceBinding(attr.localName, attr.value, currNamespace)
      }
      .orElse {
        element.attributes.toList.collectFirst {
          case (_, attr: Attr) if attr.prefix.startsWith("xmlns:") => scala.xml.NamespaceBinding(attr.name, attr.value, currNamespace)
        }
      }
  }

}
