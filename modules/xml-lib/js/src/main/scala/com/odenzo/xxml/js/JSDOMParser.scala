package com.odenzo.xxml.js

import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.implicits.*
import org.scalajs.dom
import org.scalajs.dom.{Attr, ByteString, DOMList, Document, Element, NamedNodeMap, Node, NodeFilter, Text, TreeWalker, XMLSerializer}

import scala.scalajs.js
import scala.xml.{MetaData, NamespaceBinding, NodeSeq}

/** Uses ScalaJS DOM which I think is a facade between the actual browser DOM and/or jsdom/jsdom from https://github.com/jsdom/jsdom. This
  * is more of a DOM-2-DOM translator. But, I implement it as a SAX by traversin I think.
  */
object JSDOMParser {

  val xmlMimeType = org.scalajs.dom.MIMEType.`application/xml`

  def parse(xmlStr: String): Document =
    new org.scalajs.dom.DOMParser().parseFromString(xmlStr, xmlMimeType)

  def transformToScalaXML(doc: Document): xml.Node = {
    val docType       = doc.doctype
    doc.documentElement.normalize()
    val root: Element = doc.documentElement
    recursiveDescent(root)
  }

  /** Hmmm, is it true only elems have descendants once the xmldecl (root onwards). I assume so. */
  def recursiveDescent(root: org.scalajs.dom.Element): scala.xml.Node = {
    // For Namespace stuff, need to keep a list of namespaces from xmlDecl AND I belive have global "scope" supplements by
    // heirarchial scope.... so heirarchial scopes get passed in going down removed on the way up.
    val rootsChildren: List[Node] = root.childNodes.toList // Need seperate for type inference
    val kids: Seq[scala.xml.Node] = rootsChildren.map {
      case e: Element => recursiveDescent(e)
      case n: Node    => convert(n)
    }
    closeElement(root, kids)
  }

  def closeElement(v: Element, children: Seq[scala.xml.Node]): scala.xml.Elem = {
    // FIXME: Scope Stack instead of TopScope
    scala.xml.Elem(
      prefix = v.prefix,
      label = v.tagName,
      attributes = convertAttributes(v),
      scope = xml.TopScope,
      minimizeEmpty = true,
      child = children*
    )
  }

  def convert(n: Node): scala.xml.Node = {
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
  def convertAttributes(elem: Element): MetaData = {
    val attrs: List[(String, Attr)] = elem.attributes.toList
    // Type Inference a bit wonky, need to case to MetaData from Attribute
    attrs.map((name, attr) => scala.xml.Attribute(attr.prefix, attr.name, attr.value, null)) match {
      case Nil         => xml.Node.NoAttributes
      case head :: Nil => head: MetaData
      case multi       => multi.reduce((attr1, attr2) => attr1.append(attr2))
    }

  }

  /** @return List of 1+ xmlns namespace decleration attributes of the given element */
  def findXmlns(element: Element): Option[NonEmptyList[Attr]] = {
    // xmlns:*

    val xmlns: List[Attr] = element.attributes.toList
      .collect {
        case (_, attr: Attr) if attr.prefix == "xmlns" => attr
      }
    NonEmptyList.fromList(xmlns)

  }

  def dump(n: Node): ByteString = new XMLSerializer().serializeToString(n)

  def domList2List(nl: DOMList[Node]): IndexedSeq[Node] =
    val ln = nl.length
    (0 until ln).map(nl(_))

}
