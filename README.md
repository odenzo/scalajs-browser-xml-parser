# X-Platform XML

## NEED: Parsing of simple XML response in ScalaJS Land

All I really need parser into a basic DOM like structure.

- No processing instructions
- No Validations to DTD
- No Entity Refs from DTD
- No Processing Instructions
- No fetching of external stuff (standalone=yes)
- Accept prefixes, but no real-namespace handling (even this is optional for me)

### Solution so far
1. fs2-data-xml with adaptor into scala-xml
2. Just use built in DOMParser and XPath -- facade between ScalaJS impl and existing scala-xml impl

### WIP Notes

- FS2:
  - 
  - XmlDecleration passed through
  - DocType partial support, in fact stuff between [ ...] is not passed 
  - DocType first element name is not validated,
  - All comments are stripped
  - Prefixes are passed through  
  - Namespaces go through as attributes on an elem, but nested elements don't get prefixed 
  - Doesn't guarantee a root node. Not sure it should really.
  - No DocumentStart/DocumentEnd when feeding a <?xml...> <root></root>

- Mapping to scala-xml (any better DOM)


## IDEAL: Full conformant parsing of Standalone Docs with Embedded DTD

- Gives entity references
- Maybe processing instructions.

Not sure what can be done away with really, can't we just have full functionalitry?

Well, maybe with saxon-he ... licensing maybe a problem. Then we need a DOM



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
