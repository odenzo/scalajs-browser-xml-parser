package com.odenzo.xxml

/** These are the standalone test data confirmance tests from https://www.w3.org/XML/Test/xmlconf-20020606.htm In code form since to ease
  * ScalaJS testing, with no IO, but we loose character encoding tests.
  */
object SATestData {
  val xml001    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml002    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc ></doc>"""
  val xml003    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc >"""
  val xml004    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA #IMPLIED>
]>
<doc a1="v1"></doc>"""
  val xml005    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA #IMPLIED>
]>
<doc a1 = "v1"></doc>"""
  val xml006    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA #IMPLIED>
]>
<doc a1='v1'></doc>"""
  val xml007    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>&#32;</doc>"""
  val xml008    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>&amp;&lt;&gt;&quot;&apos;</doc>"""
  val xml009    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>&#x20;</doc>"""
  val xml010    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA #IMPLIED>
]>
<doc a1="v1" ></doc>"""
  val xml011    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA #IMPLIED a2 CDATA #IMPLIED>
]>
<doc a1="v1" a2="v2"></doc>"""
  val xml012    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc : CDATA #IMPLIED>
]>
<doc :="v1"></doc>"""
  val xml013    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc _.-0123456789 CDATA #IMPLIED>
]>
<doc _.-0123456789="v1"></doc>"""
  val xml014    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc abcdefghijklmnopqrstuvwxyz CDATA #IMPLIED>
]>
<doc abcdefghijklmnopqrstuvwxyz="v1"></doc>"""
  val xml015    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc ABCDEFGHIJKLMNOPQRSTUVWXYZ CDATA #IMPLIED>
]>
<doc ABCDEFGHIJKLMNOPQRSTUVWXYZ="v1"></doc>"""
  val xml016    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc><?pi?></doc>"""
  val xml017    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc><?pi some data ? > <??></doc>"""
  val xml018    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc><![CDATA[<foo>]]></doc>"""
  val xml019    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc><![CDATA[<&]]></doc>"""
  val xml020    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc><![CDATA[<&]>]]]></doc>"""
  val xml021    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc><!-- a comment --></doc>"""
  val xml022    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc><!-- a comment ->--></doc>"""
  val xml023    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY e "">
]>
<doc>&e;</doc>"""
  val xml024    = """<!DOCTYPE doc [
<!ELEMENT doc (foo)>
<!ELEMENT foo (#PCDATA)>
<!ENTITY e "&#60;foo></foo>">
]>
<doc>&e;</doc>"""
  val xml025    = """<!DOCTYPE doc [
<!ELEMENT doc (foo*)>
<!ELEMENT foo (#PCDATA)>
]>
<doc><foo/><foo></foo></doc>"""
  val xml026    = """<!DOCTYPE doc [
<!ELEMENT doc (foo*)>
<!ELEMENT foo EMPTY>
]>
<doc><foo/><foo></foo></doc>"""
  val xml027    = """<!DOCTYPE doc [
<!ELEMENT doc (foo*)>
<!ELEMENT foo ANY>
]>
<doc><foo/><foo></foo></doc>"""
  val xml028    = """<?xml version="1.0"?>
<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml029    = """<?xml version='1.0'?>
<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml030    = """<?xml version = "1.0"?>
<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml031    = """<?xml version='1.0' encoding="UTF-8"?>
<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml032    = """<?xml version='1.0' standalone='yes'?>
<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml033    = """<?xml version='1.0' encoding="UTF-8" standalone='yes'?>
<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml034    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc/>"""
  val xml035    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc />"""
  val xml036    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>
<?pi data?>"""
  val xml037    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>
<!-- comment -->
               """
  val xml038    = """<!-- comment -->
<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>
               """
  val xml039    = """<?pi data?>
<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml040    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA #IMPLIED>
]>
<doc a1="&quot;&lt;&amp;&gt;&apos;"></doc>"""
  val xml041    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA #IMPLIED>
]>
<doc a1="&#65;"></doc>"""
  val xml042    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>&#00000000000000000000000000000000065;</doc>"""
  val xml043    = """<!DOCTYPE doc [
<!ATTLIST doc a1 CDATA #IMPLIED>
<!ELEMENT doc (#PCDATA)>
]>
<doc a1="foo
bar"></doc>"""
  val xml044    = """<!DOCTYPE doc [
<!ELEMENT doc (e*)>
<!ELEMENT e EMPTY>
<!ATTLIST e a1 CDATA "v1" a2 CDATA "v2" a3 CDATA #IMPLIED>
]>
<doc>
<e a3="v3"/>
<e a1="w1"/>
<e a2="w2" a3="v3"/>
</doc>"""
  val xml045    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA "v1">
<!ATTLIST doc a1 CDATA "z1">
]>
<doc></doc>"""
  val xml046    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA "v1">
<!ATTLIST doc a2 CDATA "v2">
]>
<doc></doc>"""
  val xml047    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>X
Y</doc>"""
  val xml048    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>]</doc>"""
  val xml049    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>£</doc>"""
  val xml050    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>เจมส์</doc>"""
  val xml051    = """<!DOCTYPE เจมส์ [
<!ELEMENT เจมส์  (#PCDATA)>
]>
<เจมส์></เจมส์>"""
  val xml052    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>𐀀􏿽</doc>"""
  val xml053    = """<!DOCTYPE doc [
<!ENTITY e "<e/>">
<!ELEMENT doc (e)>
<!ELEMENT e EMPTY>
]>
<doc>&e;</doc>"""
  val xml054    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>


<doc
></doc
>

               """
  val xml055    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<?pi  data?>
<doc></doc>"""
  val xml056    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>&#x0000000000000000000000000000000000000041;</doc>"""
  val xml057    = """<!DOCTYPE doc [
<!ELEMENT doc (a*)>
]>
<doc></doc>"""
  val xml058    = """<!DOCTYPE doc [
<!ATTLIST doc a1 NMTOKENS #IMPLIED>
<!ELEMENT doc (#PCDATA)>
]>
<doc a1=" 1     2       "></doc>"""
  val xml059    = """<!DOCTYPE doc [
<!ELEMENT doc (e*)>
<!ELEMENT e EMPTY>
<!ATTLIST e a1 CDATA #IMPLIED a2 CDATA #IMPLIED a3 CDATA #IMPLIED>
]>
<doc>
<e a1="v1" a2="v2" a3="v3"/>
<e a1="w1" a2="v2"/>
<e a1="v1" a2="w2" a3="v3"/>
</doc>"""
  val xml060    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>X&#10;Y</doc>"""
  val xml061    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>&#163;</doc>"""
  val xml062    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>&#xe40;&#xe08;&#xe21;ส์</doc>"""
  val xml063    = """<!DOCTYPE เจมส์ [
<!ELEMENT เจมส์ (#PCDATA)>
]>
<เจมส์></เจมส์>"""
  val xml064    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>&#x10000;&#x10FFFD;</doc>"""
  val xml065    = """<!DOCTYPE doc [
<!ENTITY e "&#60;">
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml066    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA #IMPLIED>
<!-- 34 is double quote -->
<!ENTITY e1 "&#34;">
]>
<doc a1="&e1;"></doc>"""
  val xml067    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>&#13;</doc>"""
  val xml068    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY e "&#13;">
]>
<doc>&e;</doc>"""
  val xml069    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!NOTATION n PUBLIC "whatever">
]>
<doc></doc>"""
  val xml070    = """<!DOCTYPE doc [
<!ENTITY % e "<!ELEMENT doc (#PCDATA)>">
%e;
]>
<doc></doc>"""
  val xml071    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a ID #IMPLIED>
]>
<doc></doc>"""
  val xml072    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a IDREF #IMPLIED>
]>
<doc></doc>"""
  val xml073    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a IDREFS #IMPLIED>
]>
<doc></doc>"""
  val xml074    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a ENTITY #IMPLIED>
]>
<doc></doc>"""
  val xml075    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a ENTITIES #IMPLIED>
]>
<doc></doc>"""
  val xml076    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a NOTATION (n1|n2) #IMPLIED>
<!NOTATION n1 SYSTEM "http://www.w3.org/">
<!NOTATION n2 SYSTEM "http://www.w3.org/">
]>
<doc></doc>"""
  val xml077    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a (1|2) #IMPLIED>
]>
<doc></doc>"""
  val xml078    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a CDATA #REQUIRED>
]>
<doc a="v"></doc>"""
  val xml079    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a CDATA #FIXED "v">
]>
<doc a="v"></doc>"""
  val xml080    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a CDATA #FIXED "v">
]>
<doc></doc>"""
  val xml081    = """<!DOCTYPE doc [
<!ELEMENT doc (a, b, c)>
<!ELEMENT a (a?)>
<!ELEMENT b (b*)>
<!ELEMENT c (a | b)+>
]>
<doc><a/><b/><c><a/></c></doc>"""
  val xml082    = """<!DOCTYPE doc [
<!ENTITY % e SYSTEM "e.dtd">
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml083    = """<!DOCTYPE doc [
<!ENTITY % e PUBLIC 'whatever' "e.dtd">
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml084    = """<!DOCTYPE doc [<!ELEMENT doc (#PCDATA)>]><doc></doc>"""
  val xml085    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY % e "<foo>">
<!ENTITY e "">
]>
<doc>&e;</doc>"""
  val xml086    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY e "">
<!ENTITY e "<foo>">
]>
<doc>&e;</doc>"""
  val xml087    = """<!DOCTYPE doc [
<!ENTITY e "<foo/&#62;">
<!ELEMENT doc (foo)>
<!ELEMENT foo EMPTY>
]>
<doc>&e;</doc>"""
  val xml088    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY e "&lt;foo>">
]>
<doc>&e;</doc>"""
  val xml089    = """<!DOCTYPE doc [
<!ENTITY e "&#x10000;&#x10FFFD;&#x10FFFF;">
<!ELEMENT doc (#PCDATA)>
]>
<doc>&e;</doc>"""
  val xml090    = """<!DOCTYPE doc [
<!ATTLIST e a NOTATION (n) #IMPLIED>
<!ELEMENT doc (e)*>
<!ELEMENT e (#PCDATA)>
<!NOTATION n PUBLIC "whatever">
]>
<doc></doc>"""
  val xml091    = """<!DOCTYPE doc [
<!NOTATION n SYSTEM "http://www.w3.org/">
<!ENTITY e SYSTEM "http://www.w3.org/" NDATA n>
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a ENTITY "e">
]>
<doc></doc>"""
  val xml092    = """<!DOCTYPE doc [
<!ELEMENT doc (a)*>
<!ELEMENT a EMPTY>
]>
<doc>
<a/>
    <a/>        <a/>


</doc>"""
  val xml093    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>


</doc>"""
  val xml094    = """<!DOCTYPE doc [
<!ENTITY % e "foo">
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a1 CDATA "%e;">
]>
<doc></doc>"""
  val xml095    = """<!DOCTYPE doc [
<!ATTLIST doc a1 CDATA #IMPLIED>
<!ATTLIST doc a1 NMTOKENS #IMPLIED>
<!ELEMENT doc (#PCDATA)>
]>
<doc a1="1  2"></doc>"""
  val xml096    = """<!DOCTYPE doc [
<!ATTLIST doc a1 NMTOKENS " 1   2       ">
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml097    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY % e SYSTEM "097.ent">
<!ATTLIST doc a1 CDATA "v1">
%e;
<!ATTLIST doc a2 CDATA "v2">
]>
<doc></doc>"""
  val xml098    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc><?pi x
y?></doc>"""
  val xml099    = """<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml100    = """<!DOCTYPE doc [
<!ENTITY e PUBLIC ";!*#@$_%" "100.xml">
<!ELEMENT doc (#PCDATA)>
]>
<doc></doc>"""
  val xml101    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY e "&#34;">
]>
<doc></doc>"""
  val xml102    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a CDATA #IMPLIED>
]>
<doc a="&#34;"></doc>"""
  val xml103    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc>&#60;doc></doc>"""
  val xml104    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a CDATA #IMPLIED>
]>
<doc a="x       y"></doc>"""
  val xml105    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a CDATA #IMPLIED>
]>
<doc a="x&#9;y"></doc>"""
  val xml106    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a CDATA #IMPLIED>
]>
<doc a="x&#10;y"></doc>"""
  val xml107    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a CDATA #IMPLIED>
]>
<doc a="x&#13;y"></doc>"""
  val xml108    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY e "
">
<!ATTLIST doc a CDATA #IMPLIED>
]>
<doc a="x&e;y"></doc>"""
  val xml109    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a CDATA #IMPLIED>
]>
<doc a=""></doc>"""
  val xml110    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY e "&#13;&#10;">
<!ATTLIST doc a CDATA #IMPLIED>
]>
<doc a="x&e;y"></doc>"""
  val xml111    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST doc a NMTOKENS #IMPLIED>
]>
<doc a="&#32;x&#32;&#32;y&#32;"></doc>"""
  val xml112    = """<!DOCTYPE doc [
<!ELEMENT doc (a | b)>
<!ELEMENT a (#PCDATA)>
]>
<doc><a></a></doc>"""
  val xml113    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ATTLIST e a CDATA #IMPLIED>
]>
<doc></doc>"""
  val xml114    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY e "<![CDATA[&foo;]]>">
]>
<doc>&e;</doc>"""
  val xml115    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY e1 "&e2;">
<!ENTITY e2 "v">
]>
<doc>&e1;</doc>"""
  val xml116    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
]>
<doc><![CDATA[
]]></doc>"""
  val xml117    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY rsqb "]">
]>
<doc>&rsqb;</doc>"""
  val xml118    = """<!DOCTYPE doc [
<!ELEMENT doc (#PCDATA)>
<!ENTITY rsqb "]]">
]>
<doc>&rsqb;</doc>"""
  val xml119    = """<!DOCTYPE doc [
<!ELEMENT doc ANY>
]>
<doc><!-- -á --></doc>"""
  val xmlSAData = List(
    xml001,
    xml002,
    xml003,
    xml004,
    xml005,
    xml006,
    xml007,
    xml008,
    xml009,
    xml010,
    xml011,
    xml012,
    xml013,
    xml014,
    xml015,
    xml016,
    xml017,
    xml018,
    xml019,
    xml020,
    xml021,
    xml022,
    xml023,
    xml024,
    xml025,
    xml026,
    xml027,
    xml028,
    xml029,
    xml030,
    xml031,
    xml032,
    xml033,
    xml034,
    xml035,
    xml036,
    xml037,
    xml038,
    xml039,
    xml040,
    xml041,
    xml042,
    xml043,
    xml044,
    xml045,
    xml046,
    xml047,
    xml048,
    xml049,
    xml050,
    xml051,
    xml052,
    xml053,
    xml054,
    xml055,
    xml056,
    xml057,
    xml058,
    xml059,
    xml060,
    xml061,
    xml062,
    xml063,
    xml064,
    xml065,
    xml066,
    xml067,
    xml068,
    xml069,
    xml070,
    xml071,
    xml072,
    xml073,
    xml074,
    xml075,
    xml076,
    xml077,
    xml078,
    xml079,
    xml080,
    xml081,
    xml082,
    xml083,
    xml084,
    xml085,
    xml086,
    xml087,
    xml088,
    xml089,
    xml090,
    xml091,
    xml092,
    xml093,
    xml094,
    xml095,
    xml096,
    xml097,
    xml098,
    xml099,
    xml100,
    xml101,
    xml102,
    xml103,
    xml104,
    xml105,
    xml106,
    xml107,
    xml108,
    xml109,
    xml110,
    xml111,
    xml112,
    xml113,
    xml114,
    xml115,
    xml116,
    xml117,
    xml118
  )
}
