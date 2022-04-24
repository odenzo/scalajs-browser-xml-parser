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

trait ElemInstances {

  /** This can be cross platform JS and JVM */
  implicit def xmlEncoder[F[_]](implicit charset: Charset = `UTF-8`): EntityEncoder[F, Elem] =
    EntityEncoder.stringEncoder
      .contramap[Elem] { node =>
        val sw = new StringWriter
        XML.write(w = sw, node = node, enc = charset.nioCharset.name, xmlDecl = true, doctype = null)
        sw.toString
      }
      .withContentType(`Content-Type`(MediaType.application.xml).withCharset(charset))

  /** Handles a message body as XML.
    *
    * Using the DOM parser exposes via scalajs.dom. This needs to replace/override the Entity Decoder for JVM.
    *
    * @return
    *   an XML element
    */
  implicit def xml[F[_]](implicit F: Concurrent[F]): EntityDecoder[F, Elem] = {
    import EntityDecoder._
    decodeBy(MediaType.text.xml, MediaType.text.html, MediaType.application.xml) { msg =>
      try {
        val elem: F[Elem] = decodeText(msg).map(XXMLParser.parse)
        DecodeResult.success(elem)
      } catch {
        case e: DOMParserException =>
          DecodeResult.failureT(MalformedMessageBodyFailure("Invalid XML", Some(e)))
        case NonFatal(e)           => DecodeResult(F.raiseError[Either[DecodeFailure, Elem]](e))
      }
    }
  }
}
