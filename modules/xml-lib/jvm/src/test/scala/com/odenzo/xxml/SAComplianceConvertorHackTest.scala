package com.odenzo.xxml

import java.nio.charset.Charset
import scala.io.{BufferedSource, Codec}
import scala.util.Try

class SAComplianceConvertorHackTest extends munit.FunSuite {

  // Test just to see if can parse XML document without errors.
  // Expand later into running JS and JVM parsing, printing results and making sure that are equal.

  test("Loading and Writing") {
    val variables: String = Range(1, 120)
      .map { i =>

        val name    = f"$i%03d.xml"
        val varName = f"xml$i%03d"

        // Meh, the files have different encodings, to test the parse I guess. So we have to actually sniff the encoding
        val codecUTF8   = Codec.UTF8
        val codecUTF16  = Codec.charset2codec(Charset.forName("UTF-16LE")) // Need UTF-16 LE for 50 at least.
        val fullPath    = "standalone/" + name
        val xml: String = Try(readResource(fullPath, Codec.UTF8))
          .recover { case _: java.nio.charset.MalformedInputException =>
            readResource(fullPath, codecUTF16)
          }
          .fold(e => throw e, v => v)

        s"val $varName = \"\"\"$xml\"\"\"  "
      }
      .mkString("\n")

    val theArray = "val xmlSAData = List(" + Range(1, 119).map(i => f"xml$i%03d").mkString(",") + ")"

    val complete = s"object SATestData { \n $variables \n $theArray \n } "
    println(complete)
  }

  def readResource(fullPath: String, codec: Codec): String = {
    val resource: BufferedSource = scala.io.Source.fromResource(fullPath)(codec)
    val lines: Iterator[String]  = resource.getLines()
    lines.mkString("\n")
  }
}
