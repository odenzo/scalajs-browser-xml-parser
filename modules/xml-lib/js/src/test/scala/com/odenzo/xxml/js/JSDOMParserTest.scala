package com.odenzo.xxml.js

import org.scalajs.dom.*
import org.w3c.dom.xpath.XPathNSResolver

import scala.annotation.tailrec
import scala.scalajs.js
import scala.scalajs.js.Object.entries
import scala.xml.Elem
import cats.effect.unsafe.implicits.global

import scala.collection.BitSet
class JSDOMParserTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.
  /*
 this.Lcom_odenzo_xxml_js_JSDOMParser$__f_filterNone = ((superClass$) => (() => {
    var this$1 = new superClass$();
    this$1.acceptNode = (function(arg) {
      return $s_Lcom_odenzo_xxml_js_JSDOMParser$$anon$1__acceptNode__Lcom_odenzo_xxml_js_JSDOMParser$$anon$1__Lorg_scalajs_dom_Node__I(this, arg)
    });
    return this$1
  }))(NodeFilter)()
   */
  test("Parse".ignore) {
    JSDOMParser.parse(TestData.input)
  }

  test("JSDOMBasic") {
    val doc: Document = JSDOMParser.parse(TestData.input)
    val root          = doc.documentElement
    scribe.info(s"Converting:\n ${JSDOMParser.dump(root)}")

    val resultSXML: xml.Node = JSDOMParser.recursiveDescent(root)
    scribe.info(s"Result scala-xml:\n${resultSXML}")
  }

}
