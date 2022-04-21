package com.odenzo.xxml

import scala.xml.MetaData

case class AttrInfo(prefix: Option[String], name: String, value: String)

/** Debugging ScalaJS is a pain, so this has functions that are only used by ScalaJS but are JVM compliant too */
object XmlFunctions {

  def makeAttributes(attrs: List[AttrInfo]): MetaData = {

    // scala.xml.Elem(v.prefix, v.tagName, convertAttributes(v), scope = null, minimizeEmpty = true)

    val preAttr: List[MetaData] = attrs.map { attr =>
      println(s"Converting Attr: $attr")
      val sxml = scala.xml.Attribute(attr.prefix, attr.name, List(scala.xml.Text(attr.value)), null)
      println(s"Into $sxml")
      sxml
    }

    println(s"Made PreAttr List: $preAttr")
    preAttr match {
      case Nil         => xml.Node.NoAttributes
      case head :: Nil => head: MetaData
      case multi       => multi.reduce((attr1, attr2) => attr1.append(attr2))
    }
  }
}
