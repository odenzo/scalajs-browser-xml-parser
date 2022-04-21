package com.odenzo.xxml

import com.odenzo.xxml.{TestData, XXMLParser}
import scala.xml.Elem

class BasicTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.

  test("Simple".ignore) {
    val resultSXML = XXMLParser.parse(TestData.simple)
    println(s"Simple scala-xml:\n${resultSXML}")
  }

  test("Comments".ignore) {
    val resultSXML = XXMLParser.parse(TestData.comments)
    println(s"Simple scala-xml:\n${resultSXML}")
  }

  test("SimpleAttributes") {
    val resultSXML = XXMLParser.parse(TestData.attributes)
    println(s"Simple scala-xml:\n${resultSXML}")
  }

}
