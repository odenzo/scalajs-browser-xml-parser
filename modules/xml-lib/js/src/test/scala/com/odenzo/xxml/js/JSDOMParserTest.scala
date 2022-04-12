package com.odenzo.xxml.js

import org.scalajs.dom.*
import org.w3c.dom.xpath.XPathNSResolver

import scala.annotation.tailrec
import scala.scalajs.js
import scala.scalajs.js.Object.entries
import scala.xml.Elem
import cats.effect.unsafe.implicits.global

import scala.collection.BitSet
class JSDOMParserTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.
  /*
 this.Lcom_odenzo_xxml_js_JSDOMParser$__f_filterNone = ((superClass$) => (() => {
    var this$1 = new superClass$();
    this$1.acceptNode = (function(arg) {
      return $s_Lcom_odenzo_xxml_js_JSDOMParser$$anon$1__acceptNode__Lcom_odenzo_xxml_js_JSDOMParser$$anon$1__Lorg_scalajs_dom_Node__I(this, arg)
    });
    return this$1
  }))(NodeFilter)()
   */
  test("Parse") {
    JSDOMParser.parse(TestData.input)
  }

  test("JSDOMBasic".ignore) {
    val doc: Document               = JSDOMParser.parse(TestData.input)
    val root                        = doc.documentElement
    def resolver(s: String): String = s
    val xpResult: XPathResult       = doc.evaluate("/node()", root, resolver _, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null)
  }

  test("TreeWalker") {
    val doc                 = JSDOMParser.parse(TestData.validXML)
    val root                = doc.documentElement
    val myShowAll: Int      = {
      import NodeFilter.*
      SHOW_TEXT | SHOW_DOCUMENT | SHOW_CDATA_SECTION | SHOW_COMMENT | SHOW_ELEMENT | SHOW_ENTITY | SHOW_DOCUMENT_TYPE
      1 | 4 | 8 | 64 | 128 | 256 | 512 | 1024
    }
    scribe.info(s"My ShowAll: $myShowAll")
    val tcursor: TreeWalker = doc.createTreeWalker(doc, myShowAll, null, true)
    JSDOMParser.showTraversal(tcursor)

  }
  // Hmmm, I guess I really want a SAX Events out, but lets double check what we can map to scala-xml
  // And XPath doesn't actualy follow the spec and give me text and comment nodes. Maybe my own traversal to see what real nodes are there

//    import scala.xml.XML
//    scala.xml.Elem // Has a list of all child Nodes (is a type of Node too) Note attributes are on element not additional nodes?
//    scala.xml.Node
//    scala.xml.Text
//    scala.xml.PCData
//    scala.xml.NodeSeq
//    @tailrec
//    def addNodeToScalaXml(node: Node): Unit = {
//      scribe.info(s"Processing Node: ${dump(node)}")
//
//      node match {
//        case data: Comment                      => scribe.info(s"Comment: $data")
//        case text: Text                         => scribe.info(s"Text: ${text.textContent}")
//        case documentType: DocumentType         =>
//        case documentFragment: DocumentFragment =>
//        case attr: Attr                         => scribe.info(s"Attr: $attr")
//        case element: Element                   => scribe.info(s"Elem: ${element.tagName}")
//        case document: Document                 =>
//        case instruction: ProcessingInstruction =>
//      }
//      val next = xpResult.iterateNext()
//      if next != null then addNodeToScalaXml(next)
//    }

  def domList2List(nl: DOMList[Node]): IndexedSeq[Node] =
    val ln = nl.length
    (0 until ln).map(nl(_))

  def dump(n: Node): ByteString = new XMLSerializer().serializeToString(n)
}
