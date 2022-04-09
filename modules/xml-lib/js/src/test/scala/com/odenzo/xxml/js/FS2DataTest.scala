package com.odenzo.xxml.js

import org.scalajs.dom.*
import org.w3c.dom.xpath.XPathNSResolver
import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.effect.unsafe.IORuntime
import cats.implicits.*
import fs2.data.xml.XmlEvent

import scala.annotation.tailrec
import scala.scalajs.js
import scala.scalajs.js.Object.entries
import scala.xml.Elem

class FS2DataTest extends munit.CatsEffectSuite {
  // Hmm. dunno how to debug test this in Node env.

  val input = """<a >
                |  <n a="attribute">text</n>
                |</a>
                |<a>
                |  <b/>
                |  test entity resolution &amp; normalization
                |</a>""".stripMargin

  test("Basic Parseing") {
    given rt: IORuntime = cats.effect.unsafe.IORuntime.global
    FSData.parse(input).flatTap(v => IO(scribe.info(s"Res: ${pprint(v)}")))

  }

  test("ScalaXML") {
    val elem = ScalaXML.parse(input)
    scribe.info(s"ScalaXML: ${pprint(elem)}")
  }
}
