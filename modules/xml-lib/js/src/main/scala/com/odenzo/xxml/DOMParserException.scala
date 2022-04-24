package com.odenzo.xxml

/** This is exposed as part of errors from XXMLParser */
case class DOMParserException(message: String, cause: Throwable) extends Exception(message, cause)

object DOMParserException {
  def apply(msg: String): DOMParserException = DOMParserException(msg, null)
}
