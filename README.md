# HTTP4S V1.xx XML EntityDecoder and EntityEncoder for ScalaJS

## The problem:
scala-xml parsing isn't functional under Scala JS, but the rest is.

## Design Criteria
+ Handle XML with internal DTD entity refs, but do not allow non-standalone documents (no external calls)
+ Enable the same "shared code" to be used for both JVM and ScalaJS

## Solution

Create XML EntityDecoder and EntityEncoder for XML in namespace `com.odenzo.xxml`.
This has ScalaJS implementation of an implicit EntityEncoder and EntityDecoder that can be imported in shared Scala code.
There is a "no-op stub" on the JVM side, so the existing HTTP4S imports are needed for JVM Decoder/Encoder

+ ScalaJS DOM is used, and handles entities, well-formed XML checked but no DTD validation at all. This allows browser based 
parsing, and NodeJS when the JSDOMNodeJSEnv is used.

```ThisBuild / jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()```

Basically, this parses the XML using the Browser DOM parsing, and then converts the Browser DOM to scala-xml DOM by returning the root elem.
Partially XML documents are not supported, you need a root element.

Once in scala-xml format you can use all the existing x-platform facilities for that, including XPath and node traversals.
If you only have ScalaJS code, the little helper for just the DOM parsing is exposed, and you can extract stuff from the Browser DOM if you 
want. Generally the scala-xml DOM is easier IMHO, although it too is littered with `null` values so be careful.

## Build Details
+ Cross compiled for ScalaJS and Scala 2.13.x and Scala 3.x 
+ TODO: Publish as Scala 3.0 which provides forward compatability

+ Minimal dependencies, only Cats and scala-jsdom 



## Example Usage

See etrade-whatever for full example HTTP4S client side usage.

TODO: 
- Minimal Client Example for POSTing XML and receiving XML in return

There is no server example, because there is no ScaleJS env server :-)


## TODO
- Clean up this mess and move to a new repo
- Add invalid XML document suite which should also pass when in no validation mode