package com.odenzo.xxml

import java.net.URL
import javax.xml.parsers.SAXParserFactory
import scala.xml.factory.XMLLoader
import scala.xml.{Elem, XML}

/** This will test the XML files converted to Strings in Scala already. THe files all pass the JVM basic test.+++++++++++++++++++
  */
class ErrorTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.
  test("XML Error") {
    XXMLParser.parse("""<root at="foo" ><bad xmlns:foo="http://foo"></bad><NotGood xmlns="http://default/"></NotGood></root>""")
  }
}
