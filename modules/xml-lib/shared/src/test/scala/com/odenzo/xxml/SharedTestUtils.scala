package com.odenzo.xxml

object SharedTestUtils {

  // This should work in NodeJSBrowser environment and JVM at least.
  // Not sure how I am going to deal real-world in browser test.

  val numFiles = 119

  val fileNames = Range(1, 119).map(i => f"$i%03d.xml")

}
