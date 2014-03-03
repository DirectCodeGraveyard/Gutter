package gutter.xml

import gutter.util.GroovyGenerationUtils

class Xml2GroovyMarkup {

    private final XmlParser parser

    private final IndentPrinter out

    private boolean complete = false

    Xml2GroovyMarkup(Writer out, int indention = 4) {
        parser = new XmlParser(false, true, true)
        parser.trimWhitespace = true
        this.out = new IndentPrinter(out, ' ' * indention, true, false)
    }

    void feed(File file) {
        feed(file.newReader())
    }

    void feed(String input, boolean stripNewLines = true) {
        if (complete) {
            throw new IllegalStateException("Xml2GroovyConverter is already complete.")
        }

        if (stripNewLines) {
            input = input.replaceAll("\n", "")
        }

        visit(parser.parseText(input))
    }

    void feed(Reader reader) {
        feed(reader.text)
    }

    @SuppressWarnings("GroovyOverlyComplexMethod")
    protected void visit(Node node) {

        out.printIndent()

        if (complete) {
            throw new IllegalStateException("Xml2GroovyConverter is already complete.")
        }

        /* Create the node */
        out.print(GroovyGenerationUtils.addQuotesIfNeeded(node.name() as String))

        /* Create the attributes as the parameters */
        if (node.attributes()) {
            out.print("(")
            node.attributes().eachWithIndex { Map.Entry<Object, Object> entry, int i ->
                /* Use Commas when needed */
                if (i != node.attributes().size() - 1) {
                    out.print(", ")
                }
                /* Create this attribute */
                out.print("'${entry.key}': '${entry.value}'")
            }

            /* Close the method call */
            if (!(node.children() instanceof NodeList && node.children().first() == node.value())) {
                out.print(")")
            }
        }

        /* Print the Value if needed */
        if (node.value() != null && node.value() instanceof List && node.children().first() instanceof String) {
            if (node.attributes()) {
                out.print(", ")
            } else {
                out.print("(")
            }
            out.print("'${node.text()}'")

            out.print(")")
        }

        /* Only continue if this node has a child */
        if (node.children().findAll {
            it instanceof Node
        }.empty) {
            return
        }

        /* Create a child set */
        out.print(" {")

        /* Handle Children Nodes */
        if (node.children()) {
            node.children().each { it ->
                if (it instanceof Node) {
                    /* New line */
                    out.println()
                    out.incrementIndent()
                    visit(it)
                    out.decrementIndent()
                }
            }
        }

        out.println()
        out.printIndent()
        out.print("}")
        /* Don't print a line on the last node */
        if (!(node.parent() && node.parent().children().last() == node)) {
            out.println()
        }
    }

    void complete() {
        complete = true
    }

    static void main(String... args) {
        if (!args) {
            println "Usage: Xml2GroovyMarkup <xml/html file>"
            System.exit(1)
        }
        def input = new File(args[0])
        if (!input.exists()) {
            println "'${input.absolutePath}' does not exist!"
            System.exit(1)
        }
        def output = new StringWriter()
        def converter = new Xml2GroovyMarkup(output, 4)
        converter.feed(input)
        converter.complete()
        print output.toString()
        System.exit(0)
    }
}
