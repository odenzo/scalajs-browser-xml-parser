package com.odenzo.xxml.js

import org.scalajs.dom
import org.scalajs.dom.{Document, Element, Node, NodeFilter, TreeWalker}

import scala.scalajs.js
import scala.xml.NodeSeq

/** Uses ScalaJS DOM which I think is a facade between the actual browser DOM and/or jsdom/jsdom from https://github.com/jsdom/jsdom.
  */
object JSDOMParser {

  /* Looks like scalajs-dom does not have the DOMParser in it. */
  val xmlMimeType = org.scalajs.dom.MIMEType.`application/xml`

  def parse(xmlStr: String): Document = {
    val parser        = new org.scalajs.dom.DOMParser
    val doc: Document = parser.parseFromString(xmlStr, xmlMimeType)
    scribe.info(s"Document: $doc")
    scribe.info(s"Document: ${pprint(doc)}")
    doc
  }

  /** Well, we have the DOM in some state, we can traverse it to build Scala XML DOM */
  def transformToScalaXML(doc: Document) = {

    /** Accept all nodes */

    val docType       = doc.doctype
    doc.documentElement.normalize()
    val root: Element = doc.documentElement
    val whatToShow    = NodeFilter.SHOW_ALL // Int, but SHOW_ALL is max unsigned long in MDN
    // expandEntityReferences is deprecated and not supported

    doc.createNodeIterator(root, whatToShow, null, true) // , NodeFilter.SHOW_ALL, filterNone, true)
    val tcursor: TreeWalker = doc.createTreeWalker(root, whatToShow, null, true)
    // tcursor.root ; tcursor.currentNode; tcursor.firstChild() / lastChile firstSibling/lastSibling /
    // next/prev/first/last siblings etc.
    // returns null on nothing. Standard traversal or some optimization given scalaxml Node = NodeSeq ?
    // Droste learning time or fn recursion?
    // Going
    showTraversal(tcursor)

    def traverseTree(tcursor: TreeWalker, acc: NodeSeq) = {

      tcursor.nextNode() // This seems to be traversal iterator, "Next visibile node" so that would be depth first left travseal of

    }

  }
  def showTraversal(tcurcer: TreeWalker): Unit =
    val cn   = tcurcer.currentNode
    scribe.info(show(cn))
    val next = tcurcer.nextNode()
    if next != null then showTraversal(tcurcer)
    ()

  /* Javascript:

var parser = new DOMParser();
var doc = parser.parseFromString(entry , "text/xml");
var term = doc.activeElement.children[0]

https://www.w3schools.com/xml/xml_parser.asp

   */

  /** This is dying on initialization. Its a ScalaJS class facade, check original. */
  lazy val filterNone = new NodeFilter {
    override def acceptNode(n: Node): Int = NodeFilter.FILTER_ACCEPT
  }

  def show(n: Node): String = {
    val ntype = JSDomNodeType.fromKey(n.nodeType).get
    // Geez, how to serialize to text a doc or node.

    val custom = ntype match
      case JSDomNodeType.ELEMENT_NODE                => s"${n.nodeName} ${n.nodeValue} Has Children: ${n.hasChildNodes()}"
      case JSDomNodeType.ATTRIBUTE_NODE              => s"${n.nodeName} ${n.nodeValue} ${n.toString}"
      case JSDomNodeType.TEXT_NODE                   => s"[${n.textContent.trim}]"
      case JSDomNodeType.CDATA_SECTION_NODE          => s"[${n.textContent.trim}] [${n.nodeValue.trim}]"
      case JSDomNodeType.ENTITY_REFERENCE_NODE       => s"LEGACY"
      case JSDomNodeType.ENTITY_NODE                 => s"LEGACY"
      case JSDomNodeType.PROCESSING_INSTRUCTION_NODE => s""
      case JSDomNodeType.COMMENT_NODE                => s"${n.textContent.trim}"
      case JSDomNodeType.DOCUMENT_NODE               => s""
      case JSDomNodeType.DOCUMENT_TYPE_NODE          => s""
      case JSDomNodeType.DOCUMENT_FRAGMENT_NODE      => s""
      case JSDomNodeType.NOTATION_NODE               => s"LEGACY"

    s"$ntype:  $custom "
  }
}
