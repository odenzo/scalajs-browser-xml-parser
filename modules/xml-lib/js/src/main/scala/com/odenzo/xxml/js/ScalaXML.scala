package com.odenzo.xxml.js

import cats.*
import cats.data.*
import cats.effect.*
import cats.effect.syntax.all.*
import cats.implicits.*
import fs2.*
import fs2.data.xml.*

object ScalaXML {

  def parse(xml: String) = {
// Linkage Fail.
    val src = scala.io.Source.fromResource(xml)
    val cpa = scala.xml.parsing.ConstructingParser.fromSource(src, false) // fromSource initializes automatically
    val doc = cpa.document()

  }

}
