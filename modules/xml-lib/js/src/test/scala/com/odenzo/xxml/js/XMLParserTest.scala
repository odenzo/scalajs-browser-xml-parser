package com.odenzo.xxml.js

import org.scalajs.dom.*
import org.w3c.dom.xpath.XPathNSResolver

import scala.annotation.tailrec
import scala.scalajs.js
import scala.scalajs.js.Object.entries
import scala.xml.Elem

class XMLParserTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.
  test("Basic Parseing") {
    val xml: Elem     = <p>Good <a style="Pushy">Hello</a><f>foo</f>Bye <!-- Comment --> </p>
    val xmlStr        = xml.toString()
    scribe.info(s"Testing XML: $xml and Str: $xmlStr")
    val doc: Document = JSDOMParser.parse(xmlStr)

    val root: Element = doc.documentElement

    def resolver(s: String): String = s
    val xpResult: XPathResult       = doc.evaluate("/node()", root, resolver _, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null)

    addNodeToScalaXml(root)
    // Hmmm, I guess I really want a SAX Events out, but lets double check what we can map to scala-xml
    // And XPath doesn't actualy follow the spec and give me text and comment nodes. Maybe my own traversal to see what real nodes are there

    inOrderTraversal(root)

    import scala.xml.XML
    scala.xml.Elem // Has a list of all child Nodes (is a type of Node too) Note attributes are on element not additional nodes?
    scala.xml.Node
    scala.xml.Text
    scala.xml.PCData
    scala.xml.NodeSeq
    @tailrec
    def addNodeToScalaXml(node: Node): Unit = {
      scribe.info(s"Processing Node: ${dump(node)}")

      node match {
        case data: Comment                      => scribe.info(s"Comment: $data")
        case text: Text                         => scribe.info(s"Text: ${text.textContent}")
        case documentType: DocumentType         =>
        case documentFragment: DocumentFragment =>
        case attr: Attr                         => scribe.info(s"Attr: $attr")
        case element: Element                   => scribe.info(s"Elem: ${element.tagName}")
        case document: Document                 =>
        case instruction: ProcessingInstruction =>
      }
      val next = xpResult.iterateNext()
      if next != null then addNodeToScalaXml(next)
    }

  }
  /*
  root match {
      case data: Comment                      => scribe.info(s"Comment: $data")
      case text: Text                         => scribe.info(s"Text: ${text.textContent}")
      case documentType: DocumentType         =>
      case documentFragment: DocumentFragment =>
      case attr: Attr                         => scribe.info(s"Attr: $attr")
      case element: Element                   => scribe.info(s"Elem: ${element.tagName}")
      case instruction: ProcessingInstruction =>
    }

   */
  // Hmm. what the tree structure, descende all child nodes <Node> Left Depth First
  def inOrderTraversal(root: Node, level: Int = 0): Unit = {
    scribe.info(s"Visting Node Level $level: $root ${root.nodeName} ${root.nodeValue} of Parent ${root.parentNode.nodeName}")
    val attr                   = root.attributes
    // I think kids includes text nodes
    val kids: IndexedSeq[Unit] = domList2List(root.childNodes).map { s =>
      scribe.info(s"Sideways => ")
      inOrderTraversal(s, level + 1)
    }
    scribe.info(s"Up")
  }

  def domList2List(nl: DOMList[Node]): IndexedSeq[Node] =
    val ln = nl.length
    (0 until ln).map(nl(_))

  def dump(n: Node): ByteString = new XMLSerializer().serializeToString(n)
}
