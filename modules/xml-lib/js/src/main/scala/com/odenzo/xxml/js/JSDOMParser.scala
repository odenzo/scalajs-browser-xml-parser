package com.odenzo.xxml.js

import org.scalajs.dom.Document

/** Well, we want to parse XML into scala-xml DOM or we could also look at the JS SAX HE parser With Yaidom we also have to<->from to JSDon
  * to ScalaXML DOM
  */
object JSDOMParser {

  /* Looks like scalajs-dom does not have the DOMParser in it. */
  val xmlMimeType           = org.scalajs.dom.MIMEType.`application/xml`
  def parse(xmlStr: String) = {
    val parser        = new org.scalajs.dom.DOMParser
    val doc: Document = parser.parseFromString(xmlStr, xmlMimeType)
    // Apparently <p> <a> </a> </p> will prematurely close the p when nested a is found, surely I misunderstand and
    // it is only <p> <p> </p> </p> that will screw it up. Otherwise useless to me, and a crude cats-parse better
    scribe.info(s"Document: $doc")
    scribe.info(s"Document: ${pprint(doc)}")
    doc
//    val errorNode     = doc.querySelector("parsererror");
//    if errorNode then throw new Exception("Parsing Error")
//    else scribe.info(doc.documentElement.nodeName)
  }

  /** Javascript */
//    const xmlStr = '<a id="a"><b id="b">hey!</b></a>';
//  const parser = new DOMParser();
//  const doc = parser.parseFromString(xmlStr, "application/xml");
//  // print the name of the root element or error message
//  const errorNode = doc.querySelector("parsererror");
//  if (errorNode) {
//    console.log("error while parsing");
//  } else {
//    console.log(doc.documentElement.nodeName);
//  }

}

/* Javascript:

var parser = new DOMParser();
var doc = parser.parseFromString(entry , "text/xml");
var term = doc.activeElement.children[0]

https://www.w3schools.com/xml/xml_parser.asp

 */
