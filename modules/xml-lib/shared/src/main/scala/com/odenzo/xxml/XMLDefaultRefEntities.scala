package com.odenzo.xxml

import scala.util.matching.Regex.Match

/** Better to regex on &*; once and select on each match rather than three regex probably */
object XMLDefaultRefEntities {

  val namedRef = """&(\p{Alpha}+);""".r

  /** Cuttent behaviour is just to leave unknown names ref undecoded since we don't deal with external (or internal!) DTD now. */
  def namedRefReplacer(m: Match): String = {
    val name = m.group(1)
    defaultEntities.getOrElse(name, m.matched)
  }

  /** This relies on the parsing stripping whitespace */
  val unicodeRef                           = """&#(\d{1,4}+);""".r
  def unicodeRefReplacer(m: Match): String = {
    val num = m.group(1).toInt
    num.toChar.toString
  }

  val unicodeRefHex                           = """&#x(\p{XDigit}{1,4}+);""".r
  def unicodeRefHexReplacer(m: Match): String = {
    // This does seem to exist in Scala JS! Yeah
    val num = Integer.parseInt(m.group(1), 16) // In Scala JS or need scodec bits?
    num.toChar.toString
  }

  val defaultEntities: Map[String, String] = Map("amp" -> "&", "quot" -> "\"", "lt" -> "<", "gt" -> ">", "apos" -> "'")

  def replace(s: String, declared: Map[String, String] = defaultEntities): String = {
    // andThen insists its a Char not a String :-(
    val s1: String = unicodeRefHex.replaceAllIn(s, unicodeRefHexReplacer _)
    val s2: String = unicodeRef.replaceAllIn(s1, unicodeRefReplacer _)
    namedRef.replaceAllIn(s2, namedRefReplacer _)
  }

  /** This does allow non start number of hex and dec digits though */
  def universalReplaces(matched: Match): String = {
    matched.group(1) match {
      case hex if hex.startsWith("#x") => Integer.parseInt(hex.drop(2), 16).toChar.toString
      case num if num.startsWith("#")  => Integer.parseInt(num.drop(1)).toChar.toString
      case name                        => defaultEntities.getOrElse(name, matched.matched) // No errors on unknown names
    }
  }
}
