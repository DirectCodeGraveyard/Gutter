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

    void callBlock(Closure closure) {
        out.incrementIndent()
        out.autoIndent = true
        closure()
        out.autoIndent = false
        out.decrementIndent()
    }

    void createIfStatement(String condition, Closure callback, Closure elseCallback = null) {
        out.println("if (${condition}) {")
        callBlock(callback)
        if (elseCallback) {
            out.println("} else {")
            callBlock(elseCallback)
        }
        out.println("}")
    }

    void createWhileLoop(String condition, Closure callback) {
        out.println("while (${condition}) {")
        callBlock(callback)
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
        callBlock(callback)
        out.println('}')
    }

    void createMethodCallWithMapParams(String name, Map<String, String> params) {
        out.print(name)
        out.print('(')
        params.eachWithIndex { key, value, index ->
            out.print(key)
            out.print(': ')
            out.print(value)
            if (index != params.size() - 1) {
                out.print(', ')
            }
        }
        out.println(')')
    }

    void createMethodCallWithMapParamsAndStringAsLastParam(String name, Map<String, String> params, String lastParam) {
        out.println("${name}(")
        params.eachWithIndex { key, value, index ->
            out.print(key)
            out.print(': ')
            out.print(value)
            out.print(', ')
        }
        out.println("'${lastParam}')")
    }

    void createClosureCallWithMapParams(String name, Map<String, String> params, Closure callback) {
        out.print(name)
        out.print('(')
        params.eachWithIndex { key, value, index ->
            out.print(key)
            out.print(': ')
            out.print(value)
            if (index != params.size() - 1) {
                out.print(', ')
            }
        }
        out.println(') {')
        out.incrementIndent()
        out.autoIndent = true
        callback()
        out.autoIndent = false
        out.decrementIndent()
        out.println('}')
    }

    void createClosureCallWithMapParamsAndStringAsLastParam(String name, Map<String, String> params, String lastParam, Closure callback) {
        out.print(name)
        out.print('(')
        params.eachWithIndex { key, value, index ->
            out.print(key)
            out.print(': ')
            out.print(value)
            if (index != params.size() - 1) {
                out.print(', ')
            }
        }
        out.print("'${lastParam}'")
        out.println(') {')
        out.incrementIndent()
        out.autoIndent = true
        callback()
        out.autoIndent = false
        out.decrementIndent()
        out.println('}')
    }

    void createImport(String className) {
        out.println("import ${className}")
    }

    void createSimpleClosureCall(String on, String name, Closure callback) {
        out.print(on)
        out.print('.')
        out.print(name)
        out.println(' {')
        out.incrementIndent()
        out.autoIndent = true
        callback()
        out.decrementIndent()
        out.autoIndent = false
        out.println('}')
    }

    void createSimpleClosureCall(String name, Closure callback) {
        out.print(name)
        out.println(' {')
        out.incrementIndent()
        out.autoIndent = true
        callback()
        out.decrementIndent()
        out.autoIndent = false
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
