package com.odenzo.xxml.js

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
