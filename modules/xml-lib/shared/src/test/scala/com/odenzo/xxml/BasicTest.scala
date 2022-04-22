package com.odenzo.xxml

import com.odenzo.xxml.{TestData, XXMLParser}

import scala.xml.{Document, Elem, Group, Node, NodeSeq, SpecialNode}

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

  test("SimpleAttributes".ignore) {
    val resultSXML = XXMLParser.parse(TestData.attributes)
    println(s"Simple scala-xml:\n${resultSXML}")
  }

  test("DuplicateAttributesDifNamespace".ignore) {
    val resultSXML = XXMLParser.parse(TestData.attributesDuplicated)
    println(s"Simple scala-xml:\n${resultSXML}")
  }

  test("NestNamespaceElem".ignore) {
    val resultSXML: scala.xml.Elem = XXMLParser.parse(TestData.nestedNamespaceElem)
    val x: NodeSeq                 = resultSXML \\ "_" // XPath Xpression
    printNamespaceOfXPath(x)
    println(s"Simple scala-xml:\n${resultSXML}")
  }

  test("NestNamespaceElemNamed") {
    val resultSXML: scala.xml.Elem = XXMLParser.parse(TestData.nestedNamespaceElemName)
    val x: NodeSeq                 = resultSXML \\ "_" // XPath Xpression
    printNamespaceOfXPath(x)
    println(s"Simple scala-xml:\n${resultSXML}")
  }

  /** This fails as it should */
  test("UnDlecared".ignore) {
    val resultSXML: scala.xml.Elem = XXMLParser.parse(TestData.namespaceUndeclared)

  }

  def printNamespaceOfXPath(v: NodeSeq): Unit = {
    v.toList.foreach {
      case elem: Elem => println(s"Was an Elem $elem with NameSpace: ${elem.namespace}")
      case _          => println(s"XPath result not an elem")
    }
  }
}
