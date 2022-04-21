# X-Platform XML

## NEED: Parsing of simple XML response in ScalaJS Land for use with HTTP4S


All I really need parser into a basic DOM like structure.

- No processing instructions
- No Validations to DTD
- No Entity Refs from DTD

- No fetching of external stuff (standalone=yes)
- Accept prefixes, but no real-namespace handling (even this is optional for me)

### Solution so far
1. Uses scalajs-dom for parsing
2. Navigates the resulting DOM and coverts to scala-xml DOM

### WIP Notes
\
## IDEAL: Full conformant parsing of Standalone Docs with Embedded DTD

- WARN if <?xml standardlone is not true>
- Parse entity decl in embedded DOCTYPE
- Leave subsequent PI in document but not action?
- Leaves unknown name EntityRefs in document (verbatum)
- Cross compile limited to 2.13 / 3 / ScalaJS (wip, using 2.13 now instead of 3)
- Change publish to 3.0 per best practice for Scala 3 libs
