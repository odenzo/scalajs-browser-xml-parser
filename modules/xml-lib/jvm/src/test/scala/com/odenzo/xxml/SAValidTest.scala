package com.odenzo.xxml

import java.net.URL
import java.util
import javax.xml.parsers.SAXParserFactory
import scala.jdk.CollectionConverters._
import scala.xml.{Elem, XML}
import scala.xml.factory.XMLLoader

/** This will test the source XML files directly in scala.xml */
class SAValidTest extends munit.FunSuite {
  // Hmm. dunno how to debug test this in Node env.

  val customXML: XMLLoader[Elem] = XML.withSAXParser {
    val factory = SAXParserFactory.newInstance()
    factory.setFeature("http://xml.org/sax/features/validation", true)
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false)
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    factory.newSAXParser()
  }

  Range(1, 120).filterNot(_ == 97).foreach { i =>
    val name = f"$i%03d.xml"
    Option(getClass.getResource(s"/standalone/$name")) match {
      case None      => println(s"No File for $name")
      case Some(url) => check(url.toString, url)
    }

  }
  def check[T](name: String, xmlUrl: URL)(implicit loc: munit.Location): Unit = {
    test(xmlUrl.toString) {
      customXML.load(xmlUrl)
    }
  }

}
