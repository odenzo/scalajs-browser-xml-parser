package com.odenzo.xxml.js

import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import fs2.data.xml.*
import fs2.*

import scala.runtime.Null$

object FSData {

  def parse(xml: String) = {

    val stream: Stream[IO, XmlEvent]       = Stream
      .emit(xml)
      .through(events[IO, String])
      .through(referenceResolver[IO]())
      .through(namespaceResolver[IO])
      .through(normalize)
      .debug(v => s"EMITTED: $v")
    val l: IO[List[fs2.data.xml.XmlEvent]] = stream.compile.toList
    l
  }

}
