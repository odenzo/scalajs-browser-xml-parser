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

  // Decl passed through, comments are stripped.
  // Namespaces go through even if not declared.
  // Namespaces not inherited.
  // XMLDocType is passed through but not actioned if external source. Maybe some public ones are
  // DocType first element name is not validated, in fact stuff between [ ...] is not passed in
  // XMLDocType event.
  // Entity decleration doesn't work
  // An, per my mistake, of course fs-data doesn't guarantee a root node. Not sure it should really.
  // TODO: Embedded DTD and some entities (builtin and declared)
  val xmlDexl = """<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
                  |<!DOCTYPE foo [
                  |
                  |<!--define the internal DTD, foo or bar both "work".-->
                  |<!-- ENforcement of schema not done of course.-->
                  |  <!ELEMENT foo (#PCDATA)>
                  |  <!ENTITY js "Jo Smith">
                  |
                  |<!--close the DOCTYPE declaration-->
                  |]>
                  | <simpleStuff>
                  | <foo>Hello</foo>
                  | <bar a="asd" b="sdf"   />
                  |  <!-- Comment --> 
                  |  <mynamespace:har>Har has undeclared namespece 
                  |     <nestElem>Does not Inherits</nestElem>
                  |  </mynamespace:har>
                  |   <!-- Treats xmlns like at attribute but doesn't action it for prefix. -->
                  |   <x xmlns:edi='http://ecommerce.example.org/schema'>
                  |  <!-- the "edi" prefix is bound to http://ecommerce.example.org/schema
                  |       for the "x" element and contents -->
                  |       Or have some teta
                  |    <yyyyyy>Y Not But me in the Tree</yyyyyy>
                  |    <edi:xx>But doesn't mind not knowing the namespace</edi:xx>
                  |  </x>
                  | <!-- &js; will give error -->
                  | <end>the &amp;</end>
                  | </simpleStuff>""".stripMargin

  val xmlExt = """<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
                 |<!DOCTYPE XHTML SYSTEM "subjects.dtd">
                 |
                 | <foo>Hello</foo>
                 | <bar a="asd" b="sdf"   />
                 |  <!-- Comment --> 
                 |  <mynamespace:har>Har has undeclared namespece 
                 |     <nestElem>Does not Inherits</nestElem>
                 |  </mynamespace:har>
                 |  <x xmlns:edi='http://ecommerce.example.org/schema'>
                 |  <!-- the "edi" prefix is bound to http://ecommerce.example.org/schema
                 |       for the "x" element and contents -->
                 |       Or have some teta
                 |    <yyyyyy>Y Not But me in the Tree</yyyyy>
                 |  </x>
                 | <end>the</end>""".stripMargin

  val input = """<root>
                | <a1>First Element</a1>
                | <b1>Second Element Same Level</b1>
                |</root>""".stripMargin

  test("Basic Parseing".ignore) {

    FSData.parse(xmlDexl).flatTap(v => IO(scribe.info(s"Res: ${pprint(v)}")))

  }

  test("ScalaXML") {
    val adaptor = DOMAdaptor
    for {
      stream <- FSData.parse(input).flatTap(v => IO(scribe.info(s"Res: ${pprint(v)}")))
      _       = adaptor.route(XmlEvent.StartDocument)
      _       = stream.foreach(adaptor.route)
      _       = adaptor.route(XmlEvent.EndDocument)
      dom     = adaptor.contextStack.top match
                  case DOMAdaptor.SyntheticRoot(roots) => scribe.info(s"Got the Root $roots")
                  // Support <a></a><b></b> becaus, well jst because really.
                  case other                           => scribe.info(s"Other: $other")
    } yield dom
    // scribe.info(s"ScalaXML: ${pprint(elem)}")
  }
}
