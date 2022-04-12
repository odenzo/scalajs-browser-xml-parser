package com.odenzo.xxml.js

import org.scalajs.dom.*
import org.w3c.dom.xpath.XPathNSResolver
import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.effect.unsafe.IORuntime
import cats.implicits.*
import com.odenzo.xxml.js.TestData.input
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

  test("Basic Parseing".ignore) {

    FSData.parse(TestData.invalidXmlDecl).flatTap(v => IO(scribe.info(s"Res: ${pprint(v)}")))

  }

  test("ScalaXML".ignore) {
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

  test("emit".ignore) {
    FSData.parse(TestData.invalidXmlDecl).flatTap(v => IO(scribe.info(s"Res: ${pprint(v)}")))
  }
}
