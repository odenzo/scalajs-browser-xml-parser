package com.odenzo.xxml.js

import org.scalajs.dom
import org.scalajs.dom.{Document, Node, NodeFilter, TreeWalker}

import scala.xml.NodeSeq

/** Well, we want to parse XML into scala-xml DOM or we could also look at the JS SAX HE parser With Yaidom we also have to<->from to JSDon
  * to ScalaXML DOM
  */
object JSDOMParser {

  /* Looks like scalajs-dom does not have the DOMParser in it. */
  val xmlMimeType           = org.scalajs.dom.MIMEType.`application/xml`
  def parse(xmlStr: String) = {
    val parser        = new org.scalajs.dom.DOMParser
    val doc: Document = parser.parseFromString(xmlStr, xmlMimeType)
    // Apparently <p> <a> </a> </p> will prematurely close the p when nested a is found, surely I misunderstand and
    // it is only <p> <p> </p> </p> that will screw it up. Otherwise useless to me, and a crude cats-parse better
    scribe.info(s"Document: $doc")
    scribe.info(s"Document: ${pprint(doc)}")
    doc
//    val errorNode     = doc.querySelector("parsererror");
//    if errorNode then throw new Exception("Parsing Error")
//    else scribe.info(doc.documentElement.nodeName)
  }

  /** Well, we have the DOM in some state, we can traverse it to build Scala XML DOM */
  def transformToScalaXML(doc: Document) = {

    /** Accept all nodes */
    val filterNone = new NodeFilter {
      override def acceptNode(n: Node): Int = NodeFilter.FILTER_ACCEPT
    }

    val docType                                                  = doc.doctype
    val root                                                     = doc.documentElement.normalize()
    doc.createNodeIterator(root, NodeFilter.SHOW_ALL, filterNone, true)
    val tcursor: TreeWalker                                      = doc.createTreeWalker(root, NodeFilter.SHOW_ALL, filterNone, true)
    // tcursor.root ; tcursor.currentNode; tcursor.firstChild() / lastChile firstSibling/lastSibling /
    // next/prev/first/last siblings etc.
    // returns null on nothing. Standard traversal or some optimization given scalaxml Node = NodeSeq ?
    // Droste learning time or fn recursion?
    // Going
    def traverseTree(tcursor: TreeWalker, acc: NodeSeq): NodeSeq = {

        tcursor.nextNode() // This seems to be traversal iterator, "Next visibile node" so that would be depth first left travseal of
      // sublings, similar to SAX, not sure if any easier/better than traversing myself TBH
      // All for today
      // Logic:
      // If I have kids build me as Tree, else I am Leaf.
      // If I have siblings, traverse sidewise with each sibling going to a Tree or Leaf
      // Pop-Up add the NodeSequence to the Parent

      Option(tcursor.firstChild()) match {
        case Some(child) => traverseTree(tcursor, acc) // Descend, tcursoe is now centered on the child node already
        case None        =>
          leafToSXMLNode(tcursor.currentNode)

//          traverse // We are a leaf node, we now traverse siblings who may not be leaf nodes

      }
    }

    def leafToSXMLNode(node: dom.Node) = {
      node.nodeType match {
      case ENTITY_REFERENCE_NODE=>  scala.xml.EntityRef(node.nodeValue)
      case ATTRIBUTE_NODE=>   scala.xml.Attribute(node.)

      case DOCUMENT_FRAGMENT_NODE=>  = js.native

      case TEXT_NODE=>  scala.xml.Text(node.textContent) // WAB CDATA?

      case ELEMENT_NODE=> // We snap the attributes here to, do not traverse them
        scala.xml.Elem(node.)

      case COMMENT_NODE=>   scala.xml.Comment(node.textContent)

      case DOCUMENT_POSITION_DISCONNECTED=>  = js.native

      case DOCUMENT_POSITION_CONTAINED_BY=>  = js.native

      case DOCUMENT_POSITION_CONTAINS=>  = js.native

      case DOCUMENT_TYPE_NODE=>  = js.native

      case DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC=>  = js.native

      case DOCUMENT_NODE=>  = js.native

      case ENTITY_NODE=>  = js.native

      case PROCESSING_INSTRUCTION_NODE=>  = js.native

      case CDATA_SECTION_NODE=>  = js.native

      case NOTATION_NODE=>  = js.native

      case DOCUMENT_POSITION_FOLLOWING=>  = js.native

      case DOCUMENT_POSITION_PRECEDING=>  = js.native
      }
      scala.xml.Node
    }
  }

  /** Javascript */
//    const xmlStr = '<a id="a"><b id="b">hey!</b></a>';
//  const parser = new DOMParser();
//  const doc = parser.parseFromString(xmlStr, "application/xml");
//  // print the name of the root element or error message
//  const errorNode = doc.querySelector("parsererror");
//  if (errorNode) {
//    console.log("error while parsing");
//  } else {
//    console.log(doc.documentElement.nodeName);
//  }

}

/* Javascript:

var parser = new DOMParser();
var doc = parser.parseFromString(entry , "text/xml");
var term = doc.activeElement.children[0]

https://www.w3schools.com/xml/xml_parser.asp

 */
