package com.odenzo.xxml

trait XXMLParserInterface {

  def parse(s: String): scala.xml.Elem
}
