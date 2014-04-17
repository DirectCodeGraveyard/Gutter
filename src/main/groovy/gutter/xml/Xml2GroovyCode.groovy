package gutter.xml

import gutter.util.GroovyCodeWriter

@SuppressWarnings("GroovyOverlyComplexMethod")
class Xml2GroovyCode {
    protected GroovyCodeWriter writer
    protected XmlParser parser

    Xml2GroovyCode(Writer output) {
        parser = new XmlParser(false, true, true)
        parser.keepIgnorableWhitespace = false
        writer = new GroovyCodeWriter(output, 4)
    }

    void feed(String input) {
        visit(parser.parseText(input))
    }

    protected void visit(Node node) {
        if (!node.parent()) { /* Root Node */
            if (node.name() == "script") { /* This is a Script */
                visitScriptNode(node)
            } else if (node.name() == "class") { /* This is a Class */
                visitClassNode(node)
            } else {
                throw new GroovyConvertException("Invalid root node: '${node.name()}'! Currently supported: 'class', 'script")
            }
        }
    }

    protected void visitScriptNode(Node node) {
        node.each(this.&visitStatementInstruction)
    }

    protected void visitStatementInstruction(Node inst) {
        switch (inst.name()) {
            case "println":
                writer.createMethodCall(null, "println", "\"${inst.text()}\"")
                break
            case "print":
                writer.createMethodCall(null, "print", "\"${inst.text()}\"")
                break
            case "variable":
                writer.createVariableDeclaration((inst.attribute("type") ?: "def").toString(), inst.attribute("name").toString(), inst.text())
                break
            case "foreach":
                def index = (inst.attribute("index") ?: false) as boolean
                writer.createClosureCall(inst.attribute("in") as String, index ? "eachWithIndex" : "each", index ? [ "it", "index" ] : [ "it" ]) { ->
                    inst.each(this.&visitStatementInstruction)
                }
                break
            case "delayed":
                def all = inst.attribute("all") as boolean
                writer.createEnclosedBlock { ->
                    if (!all) {
                        writer.createMethodCall(null, "sleep", inst.attribute('for').toString())
                    }
                    inst.each { Node st ->
                        if (all) {
                            writer.createMethodCall(null, "sleep", inst.attribute('for').toString())
                        }
                        visitStatementInstruction(st)
                    }
                }
                break
            case "code":
                writer.createCode(inst.text())
                break
            case "exit":
                writer.createMethodCall("System", "exit", (inst.attribute("code") ?: 1) as String)
                break
            case "if":
                writer.createIfStatement((inst.children().find { Node n -> n?.name() == "condition" } as Node).text(), {
                    (inst.children().findAll { Node n ->
                        n?.name() == "then"
                    } as NodeList)*.each(this.&visitStatementInstruction)
                }, {
                    (inst.children().findAll { Node n ->
                        n?.name() == "else"
                    } as NodeList)*.each(this.&visitStatementInstruction)
                })
                break
            case "while":
                writer.createWhileLoop(inst.attribute("condition") as String) {
                    inst.each(this.&visitStatementInstruction)
                }
                break
            default:
                throw new GroovyConvertException("Unknown instruction '${inst.name()}'")
                break
        }
    }

    protected static void visitClassNode(Node node) {
        throw new UnsupportedOperationException("Not Yet Implemented")
    }

    static void main(String... args) {
        def w = new StringWriter()
        new Xml2GroovyCode(w).feed(new File(args[0]).text)
        print w.toString()
        def shell = new GroovyShell()
        shell.evaluate(w.toString())
    }
}
