package com.odenzo.xxml.js

import cats.data.Chain
import fs2.data.xml.{Attr, QName, XmlEvent}

import scala.collection.mutable
import scala.xml.{Attribute, MetaData, Node, Null}

/** An Adaptor to take XMLEvents from FS2 and directly instanciate a linearized sequence of node in ScalaXML. An Node is either an elem or
  * CDATA or Text etc. Elem has all the attributes as "linked list" This assumed that entities have already been replaced and normalization
  * down from FS2. Twp approaches, a statefaul FS2 op or get a list of XMLEvent and build from there. I do the latter first then try to
  * build into a stream.
  *
  * Not worrying about pre-root node stuff yet, need to dest XML, DoctType and namespace declerations.
  */
object MyAdaptor {

  val minimizeEmptyElements = true
  final val emptyNodes      = Seq.empty[scala.xml.Node]

  type EventResult = Unit | scala.xml.Node
  case class XMLContext(currElem: scala.xml.Elem, acrruedKids: scala.collection.mutable.Seq[scala.xml.Node])

  /** MUTABLE STATE - not sure any choice or alternative */
  private[xxml] val contextStack: mutable.Stack[XMLContext] = scala.collection.mutable.Stack.empty[XMLContext]

  /** MUTABLE STATE
    *   - For aggregated linearized Nodes in the XML 'tree'.
    *   - In stream mode we will just emit and not use this.
    */
  val linearized: mutable.Seq[Node] = mutable.Seq.empty

  def route(xmlEvent: XmlEvent): EventResult = {
    xmlEvent match

      case XmlEvent.XmlDecl(version, encoding, standalone) => ()

      // acc.declaration(version, encoding.getOrElse(""), standalone.toString)
      case XmlEvent.XmlDoctype(name, docname, systemid) =>
      // scribe.warn(s"Skipping DOCTYPE: $name $docname $systemid")

      // Deal with these later, basically clear state.
      case XmlEvent.StartDocument =>
        if contextStack.nonEmpty then
          scribe.warn(s"New Document But Not Finished with Last!")
          contextStack.popAll()

      case XmlEvent.EndDocument =>
        if contextStack.nonEmpty then
          scribe.warn(s"New Document But Not Finished with Last!")
          contextStack.popAll()

      // Note: We are dropping the URL for namespaces because not sure how it comes out of FS2
      // Different models for attributes, I wonder if scala-xml is binding of JDK attribute type?
      case XmlEvent.StartTag(name, attr, false) => startElement(name, attr)
      case XmlEvent.StartTag(name, attr, true)  => startEmptyElement(name, attr)
      case XmlEvent.EndTag(name)                => endElement(name)

      case XmlEvent.XmlString(s, false)    => appendChild(scala.xml.Text(s))
      case XmlEvent.XmlString(s, true)     => appendChild(scala.xml.PCData(s))
      case XmlEvent.XmlEntityRef(name)     =>
        scribe.warn(s"All Entity Refs should be replaced by now? $name was found")
        appendChild(scala.xml.EntityRef(name))
      case XmlEvent.XmlPI(target, content) =>
        scribe.warn(s" ignoring all processing instructions: $target $content")
        scala.xml.ProcInstr(target, content)
      case XmlEvent.XmlCharRef(ref: Int)   =>
        scribe.warn(s"Skipping Character Ref, which is for a built in entity ref? (&gt;) $ref")

  }

  /** This may change state is namespaces are inhereted, otherwise not. No stack push anyway */
  def startEmptyElement(name: QName, attributes: List[Attr]): Node = {
    val xmlAttr: MetaData = AttributeTransformer.toScalaXML(attributes)
    val elem              = scala.xml.Elem(prefix = name.prefix.orNull, name.local, xmlAttr, scala.xml.TopScope, minimizeEmptyElements)
    elem
  }

  // This will get called for the root element.
  def startElement(name: QName, attributes: List[Attr]): Unit =
    val xmlAttr: MetaData = AttributeTransformer.toScalaXML(attributes)
    val curr              =
      scala.xml.Elem(name.prefix.orNull, name.local, xmlAttr, scala.xml.TopScope, minimizeEmptyElements)
    appendChild(curr)
    contextStack.push(XMLContext(curr, mutable.Seq.empty))

  def endElement(name: QName): Node = {
    // Get top of stack and make sure matches name, if so pop stack, add the element to the *current* top of stack
    // (which is not the enclosing element instead of this element). Add to its nodes* in mutable way. Ah, I see scalaxml
    // logic now. Keep
    val currOpen   = contextStack.pop()
    val currPrefix = currOpen.currElem.prefix
    val currName   = currOpen.currElem.prefix
    // Check, taking care of nulls that it matches.
    currOpen.currElem.copy(child = currOpen.acrruedKids.toSeq)
  }

  /** Add the node to the accumulating set of child nodes for the current open element. */
  def appendChild(node: scala.xml.Node): Unit = contextStack.top.acrruedKids.appended(node)

  /* State Notes:
    - I guess we need to track current namespace based on last parent namespace? forget how that works in XML.
      Every node has to specify namespace or inherits last? I use top namespace for now. Guess we need to make a
      NamespaceBinding for all the declared namespaces.  at least.
    - A immutable.List of Nodes from the linearized DOM Tree (in reverse order, or use Chain and append)
    - We have a stack of (elem, var childList) -- we add to childList on openElem, on closeElem pop and update childList
    - Parsing options I guess:
       - Minimize Empty Elements

    Context(current:scala.xml.Elem, childList: mutable.List[Node])
    Stack[Context]
   */
}

object AttributeTransformer {
  private val empty: MetaData = scala.xml.Node.NoAttributes

  def convert(a: Attr): Attribute =
    scala.xml.Attribute(a.name.prefix.getOrElse(""), a.name.local, a.value.mkString, next = empty)

  def appendAttribute(existing: MetaData, next: Attribute): MetaData =
    existing.append(next)

  def toScalaXML(toLink: List[Attr]): MetaData =
    toLink.foldLeft(empty)((l, attr) => appendAttribute(l, convert(attr)))
}
