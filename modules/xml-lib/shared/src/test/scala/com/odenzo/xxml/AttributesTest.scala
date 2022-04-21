package com.odenzo.xxml

class AttributesTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.

  test("NoPrefix") {
    val attr       = AttrInfo(None, "a", "foo")
    val resultSXML = XmlFunctions.makeAttributes(List(attr))
  }

}
