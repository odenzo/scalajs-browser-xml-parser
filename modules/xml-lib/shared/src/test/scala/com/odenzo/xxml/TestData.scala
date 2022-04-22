package com.odenzo.xxml

import scala.xml.{Elem, NodeBuffer}

object TestData {
  // Some Sample XML
  val invalidXmlDecl = """<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
                         |<!DOCTYPE foo [
                         |
                         |<!--define the internal DTD, foo or bar both "work".-->
                         |<!-- ENforcement of schema not done of course.-->
                         |  <!ELEMENT foo (#PCDATA)>
                         |  <!ENTITY js "Jo Smith">
                         |
                         |<!--close the DOCTYPE declaration-->
                         |]>
                         | <simpleStuff>
                         | <foo>Hello</foo>
                         | <bar a="asd" b="sdf"   />
                         |  <!-- Comment --> 
                         |  <mynamespace:har>Har has undeclared namespece 
                         |     <nestElem>Does not Inherits</nestElem>
                         |  </mynamespace:har>
                         |   <!-- Treats xmlns like at attribute but doesn't action it for prefix. -->
                         |   <x xmlns:edi='http://ecommerce.example.org/schema'>
                         |  <!-- the "edi" prefix is bound to http://ecommerce.example.org/schema
                         |       for the "x" element and contents -->
                         |       Or have some teta
                         |    <yyyyyy>Y Not But me in the Tree</yyyyyy>
                         |    <edi:xx>But doesn't mind not knowing the namespace</edi:xx>
                         |  </x>
                         | <!-- &js; will give error -->
                         | <end>the &amp;</end>
                         | </simpleStuff>""".stripMargin

  val validXML = """<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
                   |<!DOCTYPE XHTML SYSTEM "subjects.dtd">
                   |<rutabega>
                   | <foo c="oi">Hello</foo>
                   | <bar a="asd" b="sdf"   />
                   |  <!-- Comment --> 
                   
                   |  <x xmlns:edi='http://ecommerce.example.org/schema'>
                   |  <!-- the "edi" prefix is bound to http://ecommerce.example.org/schema
                   |       for the "x" element and contents -->
                   |    <yy>Y Not But me in the Tree</yy>
                   |    <edi:z>Ediz</edi:z>
                   |  </x>
                   | <end>the</end>
                   | </rutabega>""".stripMargin

  val withDeclaredRef = """<?xml version="1.0" encoding="UTF-8" standalone="yes" ?> 
                          |<!DOCTYPE foo [
                          |  <!ENTITY foo "FooBar">
                          |]>
                          |<root>
                          | <a1>First Element &amp; MicroHex = [&#x00B5;] MicroDec= [&#0181;] default entity ref</a1>
                          | <b1>Second Element &foo; Same Level</b1>
                          |</root>""".stripMargin

  val withUndeclaredRef = """<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>  
                            |<root>
                            | <a1>First Element &amp; default entity ref</a1>
                            | <b1>Second Element &foo; Same Level</b1>
                            |</root>""".stripMargin

  val withBuiltInRef = """<?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
                         |<root>
                         | <a1>First Element &amp; default entity ref</a1>
                         | <b1>Second Element Same Level</b1>
                         | <c1>
                         |   <ca2>Hello how are you</ca2>
                         |   <cb2>Goodbye</cb2>
                         | </c1>
                         |</root>""".stripMargin

  val nestedNamespaceElemName = """<root>
                                  | <a aa="foo" ab="bar" ns2:ab="car" xmlns:ns2="http://odenzo.com/">
                                  |  <ns2:b ab="attrV">B should be in namespace ns2 automatically?, Attributes?
                                  |   <c>Or C, even JVM shows no namespace</c>
                                  |  </ns2:b>
                                  |</a>
                                  |</root>""".stripMargin

  val nestedNamespaceElem = """<root>
                              | <a aa="foo" ab="bar" ns2:ab="car" xmlns:ns2="http://odenzo.com/">
                              |  <b ab="attrV">B should be in namespace ns2 automatically?, Attributes?
                              |   <c>Or C, even JVM shows no namespace</c>
                              |  </b>
                              |</a>
                              |</root>""".stripMargin

  val namespaceUndeclared = """<root>
                              | <foobar:a>
                              |   <c>Nested on namespace </c>
                              |</foobar:a>
                              |</root>""".stripMargin

  val attributesDuplicated = """<root>
                               | <a aa="foo" ab="bar" ns2:ab="car" xmlns:ns2="http://odenzo.com/">A Content</a>
                               |</root>""".stripMargin

  val attributes = """<root>
                     | <a aa="foo" ab="bar">A Content</a>
                     |</root>""".stripMargin

  val comments = """<root>
                   | <!-- Comment -->
                   | <b1><!-- Comment --> Second Element Same Level</b1>
                   | <c1>
                   |   <ca2>Hello</ca2>
                   |   <cb2>Goodbye</cb2>
                   | </c1>
                   |</root>""".stripMargin

  val simple = """<root>
                 | <b1>Second Element Same Level</b1>
                 | <c1>
                 |   <ca2>Hello</ca2>
                 |   <cb2>Goodbye</cb2>
                 | </c1>
                 |</root>""".stripMargin

  val xml: Elem = <p>Good <a style="Pushy">Hello</a><f>foo</f>Bye <!-- Comment --> </p>

}
