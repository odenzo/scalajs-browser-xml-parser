package com.odenzo.xxml

import com.odenzo.xxml.{TestData, XXMLParser}
import scala.xml.Elem

class BasicTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.

  test("Simple") {
    val resultSXML = XXMLParser.parse(TestData.simple)
    println(s"Simple scala-xml:\n${resultSXML}")
  }

  test("Literals") {
    val resultSXML = XXMLParser.parse(TestData.fullDoc.toString)
    println(s"Literals scala-xml:\n${resultSXML}")
  }

}
