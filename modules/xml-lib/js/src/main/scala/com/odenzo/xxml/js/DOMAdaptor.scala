package com.odenzo.xxml.js

import cats.data.Chain
import fs2.data.xml.{Attr, QName, XmlEvent}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.xml.{Attribute, Elem, MetaData, NamespaceBinding, Node, NodeSeq, Null}

/** An Adaptor to take XMLEvents from FS2-data-xml and putting in the scala-xml AST (more or less)
  *   - This doesn't do validation, doesn't deal with internal or external DTD
  *   - Doesn't deal with xmlns and manually populating the prefix for nested elements
  *
  * As of 2022-04-11 sending the full text of XML document to fs2 doesn't emit start and end documents XML Events, so be sure to surround.
  * Basic: Send: StartDocument, send all XMLEvent Send: EndDocument, call pickupDocument() the head of the nodeSeq is root (in normal case)
  */
object DOMAdaptor {

  val minimizeEmptyElements = true
  final val emptyNodes      = ListBuffer.empty[scala.xml.Node]

  // type DOMContext Synthic | XMLContext .. not sure what doing wrong.
  trait DOMContext {
    def children: Seq[scala.xml.Node]
    def addChild(x: Node): Unit
  }

  /** Change to allow multiple roots (or a headless Tree) */
  case class SyntheticRoot(accruedKids: mutable.ListBuffer[scala.xml.Node] = emptyNodes) extends DOMContext {
    override def children: Seq[Node] = accruedKids.toList // Expose as ummutable
    def addChild(x: Node): Unit      = this.accruedKids += x
  }

  case class XMLContext(currElem: scala.xml.Elem, accruedKids: scala.collection.mutable.ListBuffer[scala.xml.Node]) extends DOMContext {
    override def children: Seq[Node] = accruedKids.toList // Expose as ummutable
    def addChild(x: Node): Unit      = this.accruedKids += x
    def fullString                   = s"Element: ${currElem.label} - Kids: ${currElem.child.size} Acrrued Kids: ${accruedKids.size}"
  }

  /** MUTABLE STATE - not sure any choice or alternative, no variance on Stack? */
  val contextStack: mutable.Stack[DOMContext] = scala.collection.mutable.Stack.empty[DOMContext]

  def showContextStack: String = contextStack.map {
    case SyntheticRoot(kids) => s"SYNTHETIC ROOT w/  ROOT => ${kids}"
    case xmlctx: XMLContext  => s"XMLContext: ${xmlctx.fullString}"
    case other               => s"OTHER: $other"
  }.mkString

  /** Called after document close */
  def pickupDocument: NodeSeq = scala.xml.NodeSeq.fromSeq(contextStack.pop.children)

  def route(xmlEvent: XmlEvent): Unit = {
    scribe.info(s" ======>> Event: $xmlEvent Stack:\n$showContextStack")
    xmlEvent match

      case XmlEvent.XmlDecl(version, encoding, standalone) => ()
      // We get these, maybe should pass along, or maybe have a cow if not stand-alone

      // acc.declaration(version, encoding.getOrElse(""), standalone.toString)
      case XmlEvent.XmlDoctype(name, docname, systemid) => () // We get these but not with internal DTD info.

      case XmlEvent.StartDocument =>
        if contextStack.nonEmpty then
          scribe.warn(s"New Document But Not Finished with Last (and drained it)")
          contextStack.popAll()
        contextStack.push(SyntheticRoot())

      case XmlEvent.EndDocument =>
        if contextStack.sizeIs != 1 then
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
    appendChild(elem)
  }

  // This will get called for the root element.
  def startElement(name: QName, attributes: List[Attr]): Unit =
    val xmlAttr: MetaData = AttributeTransformer.toScalaXML(attributes)
    val curr              = scala.xml.Elem(name.prefix.orNull, name.local, xmlAttr, scala.xml.TopScope, minimizeEmptyElements)
    contextStack.push(XMLContext(curr, mutable.ListBuffer.empty))

  def endElement(name: QName): Unit = {
    contextStack.pop() match {
      case XMLContext(currElem, acrruedKids) =>
        // Safety checked skipped to make sure we are closing matching tag.
        // Move the acrrued children to the "child" NodeSeq in the present element.
        val closedElem = currElem.copy(child = acrruedKids)
        // ANd then add as a child to the current Top of Stack, which should be XMLContext of root or node in tree
        appendChild(closedElem)

      case other => throw IllegalStateException(s"Ending an Element wihle $other on top of stack")

    }
  }

  /** On the way down, this is adding a node to the parent element. which may be a synthetic root, which we will just pop and push replace
    * for now.
    */
  def appendChild(node: scala.xml.Node): Unit = contextStack.top match
    case x: SyntheticRoot => x.addChild(node)
    case x: XMLContext    => x.addChild(node)
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
