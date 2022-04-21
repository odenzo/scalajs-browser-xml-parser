# X-Platform XML Parsing

*Minimal* non-validating (aside from underlying) XML parsing that returns scala-xml Node with the XML DOM tree.

This should work on: 
  
- ScalaJS in Browser
- ScalaJS in NodewithDOM
- Scala JVM (2.13 and 3)
- Might try a Scala native too.

The use case is not for full fledged XML processing, it is designed just to deal with XML responses in HTTP4S calls.

It will not fetch external resources/DTDs.

```scala
import com.odenzo.xxml.XXMLParser
val root :scala.xml.Elem = XXMLParser.parser("""<a>THis is <b>some test</b></a>""")
```

## Status

+ Steal a test-suite from somewhere (fs-data-xml?)
+ cross scala version build
+ Understand best way to package ScalaJS and JVM so that pure code can use the lib exactly the same from whatever environment.

val scalaXmlElem = <a xmlns="http://a"><b><c>test</c></b></a>
* 
* val elem = ScalaXmlConversions.convertToElem(scalaXmlElem)
* 
* useImmutableElem(elem)


SaxonJS2:
npm install saxon-js

https://www.saxonica.com/download/javascript.xml
