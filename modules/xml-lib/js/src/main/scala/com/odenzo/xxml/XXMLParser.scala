package com.odenzo.xxml

import cats._
import cats.implicits._ // For Traverse
import org.scalajs.dom
import scala.util.control.{NonFatal, TailCalls}
import _root_.scala.xml
import _root_.scala.xml.{Elem, MetaData, NamespaceBinding, NodeSeq, Null, SAXParseException, SpecialNode, TopScope}

/** Uses ScalaJS DOM which is runnable in ScalaJS Browser and NodeJS environments to parse XML text and produce scala-xml "DOM", as a node.
  * No namespace support. This is hidden behind the EntityEncoder/Decoder In practice we can make all this package private, on;y the http4s
  * codec can be exposed.
  */
object XXMLParser {

  private val xmlMimeType = org.scalajs.dom.MIMEType.`application/xml`

  /** This throws unexpected exceptions which are generally non-recoverable. */
  def parse(s: String): scala.xml.Elem = {
    println("JS Parsing")
    val doc: dom.Document = parseText(s)
    convertToScalaXML(doc)
  }

  /** This tends to throw some exception and also give us a dummy root element with worthless error msg */
  def parseText(xmlStr: String): dom.Document = {
    val doc = new org.scalajs.dom.DOMParser().parseFromString(xmlStr, xmlMimeType)
    doc.documentElement.normalize()
    println(s"Dump: ${new dom.XMLSerializer().serializeToString(doc.documentElement)}")
    if (doc.documentElement.tagName === "parsererror") {
      val msg = doc.documentElement.textContent
      throw DOMParserException(s"Trouble Parsing XML via DOM: $msg", null)
    } else doc

  }

  def convertToScalaXML(doc: dom.Document): xml.Elem = {
    try {
      recursiveDescent(doc.documentElement, TopScope).result match {
        case e: xml.Elem =>
          val dump: String = scala.xml.Utility.serialize(e).toString
          println(s"Result:\n$dump")
          e
        case null        => throw DOMParserException(s"IMPOSSIBLE for Root to Be Null")
      }
    } catch {
      case e: DOMParserException => throw e
      case NonFatal(e)           => throw DOMParserException("Internal Error Converting XML", e)
    }

  }

  private[odenzo] def recursiveDescent(root: dom.Element, currScope: NamespaceBinding): TailCalls.TailRec[Elem] = {
    val myScope: NamespaceBinding = findXmlns(root, currScope).getOrElse(currScope)
    root.childNodes.toList
      .traverse {
        case e: dom.Element => TailCalls.tailcall(recursiveDescent(e, myScope))
        case n: dom.Node    => TailCalls.done(convert(n))
      }
      .map(large => closeElement(root, large.flatMap(_.toList)))
  }

  private[odenzo] def closeElement(v: dom.Element, children: Seq[xml.Node]): xml.Elem = {
    xml.Elem(
      prefix = v.prefix,
      label = v.localName,
      attributes = convertAttributes(v),
      scope = xml.TopScope,
      minimizeEmpty = true,
      child = children: _*
    )
  }

  private[odenzo] def convert(n: dom.Node): xml.NodeSeq = {
    n match {
      case _: dom.Document              => xml.NodeSeq.Empty               // Will never see
      case _: dom.DocumentType          => xml.NodeSeq.Empty               // Will never see since from docroot
      case _: dom.Element               => xml.NodeSeq.Empty               // Made on the closing element so ignored
      case v: dom.Comment               => xml.Comment(v.nodeValue)
      case v: dom.CDATASection          => xml.PCData(v.textContent)       // PCData != CData but closest match
      case v: dom.Text                  => xml.Text(v.textContent)         // Will still have XML Refs (built-in only)
      case v: dom.ProcessingInstruction => xml.ProcInstr(v.target, v.data) // What to do with these? Could check ?xml is standalone?
    }
  }

  /** Converts scalajs.dom attributes to scala-xml attributes, leaves any xmlns:nameSpaceUri attributes in. */
  private[odenzo] def convertAttributes(elem: dom.Element): MetaData = {
    val md: List[xml.MetaData] =
      elem.attributes.toList
        .map(_._2)
        .map(a => xml.Attribute(Option(a.prefix), a.localName, xml.Text(a.value), Null))

    md match {
      case Nil   => xml.Node.NoAttributes
      case multi => multi.reduceLeft((attr1, attr2) => attr1.append(attr2))
    }

  }

  /** scala-xml namespace is always empty, even on JVM, so not sure the point of this. (prefixed or not) Perhaps it will enable some
    * scala-xml sanity checks so leaving it in. Note that I also leave the raw attributes in the elements
    * @return
    *   Maybe 1 binding (with or without prefix) on the given element
    */
  private[odenzo] def findXmlns(element: dom.Element, currNamespace: xml.NamespaceBinding): Option[NamespaceBinding] = {
    element.attributes.toList.collectFirst {
      case (_, attr: dom.Attr) if attr.name != null && attr.name.contains("xmlns") =>
        Option(attr.prefix) match { // Deal will null
          case None          => xml.NamespaceBinding(null, attr.value, currNamespace) // Default Namespace
          case Some("xmlns") => xml.NamespaceBinding(attr.localName, attr.value, currNamespace)
          case Some(_)       => throw DOMParserException("Impossible Case")
        }
    }
  }

}
