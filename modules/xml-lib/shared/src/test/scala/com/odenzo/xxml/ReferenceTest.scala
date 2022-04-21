package com.odenzo.xxml

import com.odenzo.xxml.TestData
import com.odenzo.xxml.XXMLParser
import scala.xml.Elem

class ReferenceTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.

  test("DeclaredRef".ignore) {
    // This actually works on JVM DOMParser!
    val resultSXML = XXMLParser.parse(TestData.withDeclaredRef)
    println(s"DeclaredRef scala-xml:\n${resultSXML}")
  }

  test("DefeultRef".ignore) {
    // Note that the entity ref IS NOT EXPANDED
    val resultSXML = XXMLParser.parse(TestData.withBuiltInRef)
    println(s"DefeultRef scala-xml:\n${resultSXML}")
  }

  test("UndeclaredRef".ignore) {
    // This should fail on both and does, SAXParseException and for JS <parseerror>
    val resultSXML = XXMLParser.parse(TestData.withUndeclaredRef)
    println(s"UndeclaredRef scala-xml:\n${resultSXML}")
  }

}
