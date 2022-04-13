package com.odenzo.xxml.js

import org.scalajs.dom.Node

enum JSDomNodeType(val key: Int) {
  case ELEMENT_NODE extends JSDomNodeType(1)
  case ATTRIBUTE_NODE extends JSDomNodeType(2)
  case TEXT_NODE extends JSDomNodeType(3)
  case CDATA_SECTION_NODE extends JSDomNodeType(4)
  case ENTITY_REFERENCE_NODE extends JSDomNodeType(5)
  case ENTITY_NODE extends JSDomNodeType(6)
  case PROCESSING_INSTRUCTION_NODE extends JSDomNodeType(7)
  case COMMENT_NODE extends JSDomNodeType(8)
  case DOCUMENT_NODE extends JSDomNodeType(9)
  case DOCUMENT_TYPE_NODE extends JSDomNodeType(10)
  case DOCUMENT_FRAGMENT_NODE extends JSDomNodeType(11)
  case NOTATION_NODE extends JSDomNodeType(12)

}

object JSDomNodeType:
  def fromKey(k: Int): Option[JSDomNodeType] = JSDomNodeType.values.find((v: JSDomNodeType) => v.key == k)

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
