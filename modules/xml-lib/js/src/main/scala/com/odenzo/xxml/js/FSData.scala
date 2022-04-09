package com.odenzo.xxml.js

import cats.effect.*
import cats.effect.syntax.all.*
import cats.*
import cats.data.*
import cats.syntax.all.*
import fs2.data.xml.*
import fs2.*

import scala.runtime.Null$
import scala.xml.{Attribute, MetaData, Null}
import scala.xml.parsing.MarkupHandler
object FSData {

  def parse(xml: String) = {
    val acc: FS2Binding              = new FS2Binding()
    val stream: Stream[IO, XmlEvent] = Stream
      .emit(xml)
      .through(events[IO, String])
      .through(referenceResolver[IO]())
      .through(normalize)

    val l: IO[List[fs2.data.xml.XmlEvent]] = stream.compile.toList
    l
  }

}

class FS2Binding extends scala.xml.parsing.NoBindingFactoryAdapter {}
