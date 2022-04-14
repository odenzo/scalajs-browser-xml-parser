package com.odenzo.xxml

import org.scalajs.dom.*
import scala.xml.{Elem, MetaData, NamespaceBinding, SpecialNode}

/** Uses ScalaJS DOM which is runnable in ScalaJS Browser and NodeJS environments to parse XML text and produce scala-xml "DOM", as a node.
  * No namespace support.
  */
object XXMLParser extends com.odenzo.xxml.XXMLParserInterface {

  private val xmlMimeType = org.scalajs.dom.MIMEType.`application/xml`

  /** This throws unexpected exceptions which are generally non-recoverable. */
  override def parse(s: String): Elem = {
    val doc: Document = parseText(s)
    transformToScalaXML(doc) match {
      case elem: Elem        => elem: Elem
      case xml.Group(nodes)  => throw new IllegalStateException(s"IMPOSSIBLE for Root to Be Group Node: $nodes")
      case node: SpecialNode => throw new IllegalStateException(s"IMPOSSIBLE for Root to Be Special Node: $node")
      case other             => throw new IllegalStateException("IMPOSSIBLE for Root to Be Other Node: $other")
    }
  }

  def parseText(xmlStr: String): Document =
    new org.scalajs.dom.DOMParser().parseFromString(xmlStr, xmlMimeType)

  def transformToScalaXML(doc: Document): xml.Node = {
    val docType       = doc.doctype
    doc.documentElement.normalize()
    val root: Element = doc.documentElement
    recursiveDescent(root)
  }

  /** Is it true only elems have descendants once the xmldecl (root onwards after normalizing?). I assume so. */
  private[odenzo] def recursiveDescent(root: org.scalajs.dom.Element): scala.xml.Node = {
    val rootsChildren: List[Node] = root.childNodes.toList // Seperated for type inference
    val kids: Seq[scala.xml.Node] = rootsChildren.map { // Note that this may be empty list of children.
      case e: Element => recursiveDescent(e)
      case n: Node    => convert(n)
    }
    closeElement(root, kids)
  }

  private[odenzo] def closeElement(v: Element, children: Seq[scala.xml.Node]): scala.xml.Elem = {
    // FIXME: I am not keeping track of Scope Stack, not sure the purpose really since not validating
    scala.xml.Elem(
      prefix = v.prefix,
      label = v.tagName,
      attributes = convertAttributes(v),
      scope = xml.TopScope,
      minimizeEmpty = true,
      child = children*
    )
  }

  private[odenzo] def convert(n: Node): scala.xml.Node = {
    import org.scalajs.dom.*
    n match
      // case v: Document              => scala.xml.NodeSeq.Empty
      // case v: DocumentType          => scala.xml.dtd.DocType(v.name, v.publicId, Seq.empty)
      case v: Comment               => scala.xml.Text(v.nodeValue)
      case v: Text                  => scala.xml.Text(v.nodeValue)
      case v: ProcessingInstruction => scala.xml.ProcInstr(v.target, v.data)
      case v: Element               => scala.xml.Elem(v.prefix, v.tagName, convertAttributes(v), scope = null, minimizeEmpty = true)
  }

  /** Converts scalajs.dom attributes to scala-xml attributes, leaves any xmlns:nameSpaceUri attributes in. */
  private[odenzo] def convertAttributes(elem: Element): MetaData = {
    val attrs: List[(String, Attr)] = elem.attributes.toList
    // Type Inference a bit wonky, need to case to MetaData from Attribute
    attrs.map((name, attr) => scala.xml.Attribute(attr.prefix, attr.name, attr.value, null)) match {
      case Nil         => xml.Node.NoAttributes
      case head :: Nil => head: MetaData
      case multi       => multi.reduce((attr1, attr2) => attr1.append(attr2))
    }

  }

  /** TODO: Uncertain the utility of doing namespaces, instead of using a Stack scala.xml thread the namespace bindings via parent But one
    * elem can have multiple xmlns and only one can be xmlns="foo" the rest xmlbs:prefix="bar" Leaving this here for now incase I want to go
    * back, and make my own immutable Stack[(active,default)] for namespaces.
    * @return
    *   List of 1+ xmlns namespace decleration attributes of the given element
    */
  private[odenzo] def findXmlns(element: Element, currNamespace: scala.xml.NamespaceBinding): Unit = {
    // xmlns:*
    val defaultXmlns: NamespaceBinding = element.attributes.toList
      .collectFirst {
        case (_, attr: Attr) if attr.prefix == "xmlns" => scala.xml.NamespaceBinding(attr.name, attr.value, currNamespace)
      }
      .getOrElse(currNamespace)

    val explicitXmlns: List[NamespaceBinding] = element.attributes.toList.collect {
      case (_, attr: Attr) if attr.prefix.startsWith("xmlns:") => scala.xml.NamespaceBinding(attr.name, attr.value, currNamespace)
    }

    ()

  }

  /** Reminder facade :-) */
  private[odenzo] def dump(n: Node): ByteString = new XMLSerializer().serializeToString(n)

}
