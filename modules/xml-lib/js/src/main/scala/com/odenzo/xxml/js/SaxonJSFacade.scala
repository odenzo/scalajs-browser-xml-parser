package com.odenzo.xxml.js

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

/** We can get Saxon from NPM for Node or use the plain Javascript. I want to try plain Javascript, no node, and see if can test that in IJ.
  * (which would use a non-browser runtime)
  */
object SaxonJSFacade {

  /** Stuff to parse some XML */
  // SaxonJS.getResource(options)
  // options = (text: "<xm>... </xm>", type: "xml"})
  // .then(result => console.log("Transformation result: " + result.principalResult.toString()))
  // .catch(err => console.log("Transformation failed: " + err));

//  @JSName("SaxonJS")
//  @js.native
//  object SaxonJS extends js.Object {
//    def getResource(text: String, `type`: String): js.Any = js.native
//
//  }

  /** Stuff to serialize XML (back to text, maybe to JSON? */
  // SaxonJS.serialize(value, options?)
  // (for example {method:"xml", encoding:"iso-8859-1"} t

  /** Stuff to apply an XPath which we will use for all data extraction I guess */

  // SaxonJS.XPath.evaluate(xpath, contextItem ?, options ?)
}
