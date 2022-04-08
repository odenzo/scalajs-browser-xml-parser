package com.odenzo.xxml.jvm

/** Parses basic XML to ScalaXML. ScalaXML is x-platform, but the parser isn't. */
object XXMLParser {

  import scala.xml.{XML, Elem}
  import scala.xml.factory.XMLLoader
  import javax.xml.parsers.{SAXParser, SAXParserFactory}

  val customXML: XMLLoader[Elem] = XML.withSAXParser {
    val factory = SAXParserFactory.newInstance()
    factory.setFeature("http://xml.org/sax/features/validation", false)
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false)
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    factory.newSAXParser()
  }

  def parse(s: String): Elem = customXML.loadString(s)
}
