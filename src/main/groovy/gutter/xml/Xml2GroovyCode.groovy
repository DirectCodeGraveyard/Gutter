package gutter.xml

@SuppressWarnings("GroovyOverlyComplexMethod")
class Xml2GroovyCode {

    protected IndentPrinter out

    protected XmlParser parser

    Xml2GroovyCode(Writer output) {
        out = new IndentPrinter(output, "    ")
        parser = new XmlParser(false, true, true)
        parser.keepIgnorableWhitespace = false
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
                createMethodCall(null, "println", "\"${inst.text()}\"")
                break
            case "print":
                createMethodCall(null, "print", "\"${inst.text()}\"")
                break
            case "variable":
                createVariableDeclaration((inst.attribute("type") ?: "def").toString(), inst.attribute("name").toString(), inst.text())
                break
            case "foreach":
                def index = (inst.attribute("index") ?: false) as boolean
                createClosureCall(inst.attribute("in") as String, index ? "eachWithIndex" : "each", index ? [ "it", "index" ] : [ "it" ]) { ->
                    inst.each(this.&visitStatementInstruction)
                }
                break
            case "delayed":
                def all = inst.attribute("all") as boolean
                createEnclosedBlock { ->
                    if (!all) {
                        createMethodCall(null, "sleep", inst.attribute('for').toString())
                    }
                    inst.each { st ->
                        if (all) {
                            createMethodCall(null, "sleep", inst.attribute('for').toString())
                        }
                        visitStatementInstruction(st)
                    }
                }
                break
            case "code":
                out.println(inst.text())
                break
            case "exit":
                createMethodCall("System", "exit", (inst.attribute("code") ?: 1) as String)
                break
            case "if":
                createIfStatement((inst.children().find { Node n -> n?.name() == "condition" } as Node).text(), {
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
                createWhileLoop(inst.attribute("condition") as String) {
                    inst.each(this.&visitStatementInstruction)
                }
                break
            default:
                throw new GroovyConvertException("Unknown instruction '${inst.name()}'")
                break
        }
    }

    protected void createIfStatement(String condition, Closure callback, Closure elseCallback = null) {
        out.print("if(")
        out.print(condition)
        out.println(') {')
        out.incrementIndent()
        out.autoIndent = true
        callback()
        out.autoIndent = false
        out.decrementIndent()
        if (elseCallback) {
            out.println("} else {")
            out.incrementIndent()
            out.autoIndent = true
            elseCallback()
            out.autoIndent = false
            out.decrementIndent()
        }
        out.println("}")
    }

    protected void createWhileLoop(String condition, Closure callback) {
        out.print("while(")
        out.print(condition)
        out.println(') {')
        out.incrementIndent()
        out.autoIndent = true
        callback()
        out.autoIndent = false
        out.decrementIndent()
        out.println("}")
    }

    protected void createMethodCall(String on = null, String methodName, String... paramExpr) {
        out.println("${on ? on + '.' : ''}${methodName}(${paramExpr.join(', ')})")
    }

    protected void createEnclosedBlock(Closure callback) {
        out.println("{ ->")
        out.incrementIndent()
        out.autoIndent = true
        callback()
        out.autoIndent = false
        out.decrementIndent()
        out.println("}()")
    }

    protected void createClosureCall(String on = null, String name, List<String> params = [], Closure callback) {
        if (on)
            out.print "${on}."
        out.print(name)
        out.print(' { ')
        out.print(params.join(', '))
        out.println(' ->')
        out.incrementIndent()
        out.autoIndent = true
        callback()
        out.autoIndent = false
        out.decrementIndent()
        out.println('}')
    }

    protected void createVariableDeclaration(String type = "def", String name, String equalsExpr) {
        out.print(type ?: 'def')
        out.print(' ')
        createPropertyDeclaration(name, equalsExpr)
    }

    protected void createPropertyDeclaration(String name, String equalsExpr) {
        out.println("${name} = ${equalsExpr}")
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
