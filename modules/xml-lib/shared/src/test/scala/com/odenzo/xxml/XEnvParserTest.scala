package com.odenzo.xxml

import com.odenzo.xxml.TestData
import com.odenzo.xxml.XXMLParser
import scala.xml.Elem

class XEnvParserTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.

  test("JSDOMBasic") {
    val resultSXML = XXMLParser.parse(TestData.input)
    println(s"Result scala-xml:\n${resultSXML}")
  }

}
