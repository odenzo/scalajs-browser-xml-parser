package com.odenzo.xxml.js

import org.scalajs.dom.Document

import scala.xml.Elem

class XMLParserTest extends munit.FunSuite {

  test("Basic Parseing") {
    val xml: Elem     = <p>Good <a style="Pushy">Hello</a>Bye </p>
    val xmlStr        = xml.toString()
    scribe.info(s"Testing XML: $xml and Str: $xmlStr")
    val doc: Document = JSDOMParser.parse(xmlStr)
    scribe.info(s"${doc.documentElement.tagName}")
  }

}
