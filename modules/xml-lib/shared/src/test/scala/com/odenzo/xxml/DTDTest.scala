package com.odenzo.xxml

import scala.xml.{Elem, NodeSeq}

class DTDTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.

  test("DTD") {
    val resultSXML: Elem = XXMLParser.parse(TestData.withDeclaredRef)
    val a1               = resultSXML \\ "a1"
    println(s"Simple scala-xml:\n${resultSXML}")
    val s1Text           = a1.text
    println(s"A1 Text: $s1Text")
    println(s"A1= $a1")
  }

}
