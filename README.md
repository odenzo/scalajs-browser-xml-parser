# X-Platform XML

A *very* based ScalaJS and Scala JVM XML Library.

Its focus is on parsing XML into the existing x-platform scala-xml library.




Once get to ScalaXML DOM, then maybe   libraryDependencies += "com.github.geirolz" %% "advxml-core" % 2.4.2 can be made ScalaJS


yaidom and yaidom2 look possible.


That is based off Saxon-HE which has JVM counterpart.


val scalaXmlElem = <a xmlns="http://a"><b><c>test</c></b></a>
* 
* val elem = ScalaXmlConversions.convertToElem(scalaXmlElem)
* 
* useImmutableElem(elem)


SaxonJS2:
npm install saxon-js

https://www.saxonica.com/download/javascript.xml
