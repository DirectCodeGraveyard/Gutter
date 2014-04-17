package gutter.util

/**
 * Provides methods to write Groovy Code
 */
class GroovyCodeWriter {
    private Writer actual
    private IndentPrinter out

    GroovyCodeWriter(Writer actual, int indent) {
        this.actual = actual
        this.out = new IndentPrinter(actual, ' ' * indent)
    }

    void createIfStatement(String condition, Closure callback, Closure elseCallback = null) {
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

    void createWhileLoop(String condition, Closure callback) {
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

    void createEnclosedBlock(Closure callback) {
        out.println("{ ->")
        out.incrementIndent()
        out.autoIndent = true
        callback()
        out.autoIndent = false
        out.decrementIndent()
        out.println("}()")
    }

    void createClosureCall(String on = null, String name, List<String> params = [], Closure callback) {
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

    void createVariableDeclaration(String type = "def", String name, String equalsExpr) {
        out.print(type ?: 'def')
        out.print(' ')
        createPropertyDeclaration(name, equalsExpr)
    }

    void createPropertyDeclaration(String name, String equalsExpr) {
        out.println("${name} = ${equalsExpr}")
    }

    void createCode(String code) {
        out.println(code)
    }
}
