package com.odenzo.xxml

import com.odenzo.xxml.XMLDefaultRefEntities.defaultEntities

class XmlDefaultEntitiesTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.

  val defaultTestString = defaultEntities.toList
    .map { case (name, expands) =>
      (s"&$name;,$expands")
    }
    .mkString(":")

  test("TestData") {
    println(s"TestSte scala-xml:\n$defaultTestString")
    val resultSXML = XMLDefaultRefEntities.replace(defaultTestString)
    println(s"Result: $resultSXML")
  }

  test("Unknown Name Behaviour") {
    val str        = defaultTestString + " &what;"
    println(s"TestSte scala-xml:\n$str")
    val resultSXML = XMLDefaultRefEntities.replace(str)
    println(s"Result: $resultSXML")
  }

  test("Hex Unicode") {
    val src        = "&#x00B5;"
    val target     = "\u00B5" // UTF-16 Hex
    println(s"TestSte scala-xml:\n$src")
    val resultSXML = XMLDefaultRefEntities.replace(src)
    println(s"Result: $resultSXML")
  }

//
//  test("ReferenceDefault") {
//    val resultSXML = XXMLParser.parse(TestData.withBuiltInRef.toString)
//    println(s"Literals scala-xml:\n${resultSXML}")
//  }

}
