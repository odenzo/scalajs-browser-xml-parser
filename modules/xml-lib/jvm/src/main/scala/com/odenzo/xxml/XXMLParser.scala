package com.odenzo.xxml

/** Parses basic XML to ScalaXML. ScalaXML is x-platform, but the parser isn't. */
object XXMLParser extends com.odenzo.xxml.XXMLParserInterface {

  import javax.xml.parsers.{SAXParser, SAXParserFactory}
  import scala.xml.factory.XMLLoader
  import scala.xml.{Elem, XML}

  val customXML: XMLLoader[Elem] = XML.withSAXParser {
    val factory = SAXParserFactory.newInstance()
    factory.setFeature("http://xml.org/sax/features/validation", true)
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false)
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    factory.newSAXParser()
  }

  def parse(s: String): Elem = {
    println("JVM Parsing")
    customXML.loadString(s)
  }
}
