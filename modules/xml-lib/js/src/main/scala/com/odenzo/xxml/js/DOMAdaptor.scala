package com.odenzo.xxml.js

import cats.data.Chain
import fs2.data.xml.{Attr, QName, XmlEvent}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.xml.{Attribute, Elem, MetaData, NamespaceBinding, Node, NodeSeq, Null}

/** An Adaptor to take XMLEvents from FS2 and directly instanciate a linearized sequence of node in ScalaXML. An Node is either an elem or
  * CDATA or Text etc. Elem has all the attributes as "linked list" This assumed that entities have already been replaced and normalization
  * down from FS2. Twp approaches, a statefaul FS2 op or get a list of XMLEvent and build from there. I do the latter first then try to
  * build into a stream.
  *
  * Not worrying about pre-root node stuff yet, need to dest XML, DoctType and namespace declerations. I guess its up to me to find xmlns
  * attributes and deal with nested scopes, adding prefixes to nested as needed?
  */
object DOMAdaptor {

  val minimizeEmptyElements = true
  final val emptyNodes      = ListBuffer.empty[scala.xml.Node]

  // type DOMContext Synthic | XMLContext .. not sure what doing wrong.
  trait DOMContext {
    def addChild(x: Node): Unit
  }

  /** Change to allow multiple roots (or a headless Tree) */
  case class SyntheticRoot(root: Option[scala.xml.Elem])                                                            extends DOMContext {
    def addChild(x: Node): Unit = throw Throwable("Add Child to Synthetic Root Should never be called?")
  }
  case class XMLContext(currElem: scala.xml.Elem, acrruedKids: scala.collection.mutable.ListBuffer[scala.xml.Node]) extends DOMContext {
    def addChild(x: Node): Unit = this.acrruedKids += x
    def fullString              = s"Element: ${currElem.label} - Kids: ${currElem.child.size} Acrrued Kids: ${acrruedKids.size}"
  }

  /** MUTABLE STATE
    *   - For aggregated linearized Nodes in the XML 'tree'.
    *   - In stream mode we will just emit and not use this.
    */

  /** MUTABLE STATE - not sure any choice or alternative, no variance on Stack? */
  val contextStack: mutable.Stack[DOMContext] = scala.collection.mutable.Stack.empty[DOMContext]

  def showContextStack: String = contextStack.map {
    case SyntheticRoot(None)       => s"SYNTHETIC ROOT"
    case SyntheticRoot(Some(root)) => s"SYNTHETIC ROOT w/  ROOT => ${root}"
    case xmlctx: XMLContext        => s"XMLContext: ${xmlctx.fullString}"
    case other                     => s"OTHER: $other"
  }.mkString

  def route(xmlEvent: XmlEvent): Unit = {
    scribe.info(s" ======>> Event: $xmlEvent Stack:\n$showContextStack")
    val result = xmlEvent match

      case XmlEvent.XmlDecl(version, encoding, standalone) => ()
      // We get these, maybe should pass along, or maybe have a cow if not stand-alone

      // acc.declaration(version, encoding.getOrElse(""), standalone.toString)
      case XmlEvent.XmlDoctype(name, docname, systemid) => () // We get these but not with internal DTD info.

      case XmlEvent.StartDocument =>
        if contextStack.nonEmpty then
          scribe.warn(s"New Document But Not Finished with Last!")
          contextStack.popAll()
        contextStack.push(SyntheticRoot(None))

      case XmlEvent.EndDocument =>
        if contextStack.nonEmpty then
          scribe.warn(s"Ending document but still have open element")
          contextStack.popAll()

      // This is where we should return the document, or more precisly, release the synthetic root value (which could be Seq(..) instead
      // of root

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

    scribe.info(s" <<====== Event: $xmlEvent Stack:\n$showContextStack")
    ()
  }

  /** This may change state is namespaces are inhereted, otherwise not. No stack push anyway */
  def startEmptyElement(name: QName, attributes: List[Attr]): Unit = {
    val xmlAttr: MetaData = AttributeTransformer.toScalaXML(attributes)
    val elem              = scala.xml.Elem(prefix = name.prefix.orNull, name.local, xmlAttr, scala.xml.TopScope, minimizeEmptyElements)
    val context: DOMContext = XMLContext(elem, emptyNodes)

    // This is a start and a stop, so we are really just adding a child to the current top of the stack.
    //
    contextStack.top match {
      case ctx: XMLContext       => ctx.addChild(elem)
      case SyntheticRoot(None)       =>
        contextStack.pop()
        contextStack.push(context)

      /*  <a/> <b/>  case -- still debating allowing   */
      case SyntheticRoot(Some(root)) =>
        // Can only have one root note, although fs-data-xml doesn't enforce this I do.
        throw Throwable("Added to SyntheticRoot when already root node exists.")

    }
  }

  // This will get called for the root element.
  def startElement(name: QName, attributes: List[Attr]): Unit =
    val xmlAttr: MetaData = AttributeTransformer.toScalaXML(attributes)
    val curr              = scala.xml.Elem(name.prefix.orNull, name.local, xmlAttr, scala.xml.TopScope, minimizeEmptyElements)
   // appendChild(curr)
    contextStack.push(XMLContext(curr, mutable.ListBuffer.empty))

  def endElement(name: QName): Unit = {
    contextStack.pop() match {
      case XMLContext(currElem, acrruedKids) =>
        // Safety checked skipped to make sure we are closing matching tag.
        // Move the acrrued children to the "child" NodeSeq in the present element.
        val closedElem = currElem.copy(child = acrruedKids)
        // ANd then add as a child to the current Top of Stack, which should be XMLContext of root or node in tree
        contextStack.top.addChild(closedElem)

      case x @ SyntheticRoot(Some(_)) => throw IllegalStateException("Closing SynthRoot with Root in Synthetic")
      case SyntheticRoot(None)        => throw IllegalStateException("Closing SynthRoot with No Synthetic")
    }
  }

  /** On the way down, this is adding a node to the parent element.
    * which may be a synthetic root, which we will just pop and push replace for now. */
  def appendChild(node: scala.xml.Node): Unit = contextStack.top match
    case x:SyntheticRoot(root) => ???
    case XMLContext(currElem, acrruedKids) => ???
    case _ => ???(node)

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
