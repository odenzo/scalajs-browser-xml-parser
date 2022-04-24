package com.odenzo.xxml

import cats.effect._
import cats.effect.syntax.all._
import cats._
import cats.data._
import cats.implicits._
import cats.effect.Concurrent
import org.http4s.{Charset, DecodeFailure, DecodeResult, EntityDecoder, EntityEncoder, MalformedMessageBodyFailure, Media, MediaType}
import org.http4s.Charset.`UTF-8`
import org.http4s.headers.`Content-Type`

import java.io.StringWriter
import scala.util.control.NonFatal
import scala.xml.Elem
import scala.xml.XML

/** This is a NO-OP stub to allow same code in ScalaJS and Scala without stepping on HTTP4S namespace
  * {{{import com.odenzo.xxml.ElemInstances._}}} {{{import com.odenzo.xxml.ElemInstances.given}}} for scala 3 In addition to the regular
  * (JVM) Elem instances. <<TODO>>
  */
trait ElemInstances {}

object ElemInstances extends ElemInstances
