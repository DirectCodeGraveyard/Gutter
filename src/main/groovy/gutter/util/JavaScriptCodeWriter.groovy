package gutter.util

class JavaScriptCodeWriter {
    private final Writer actual
    private final IndentPrinter out

    JavaScriptCodeWriter(Writer output, int indention = 4) {
        this.actual = output
        this.out = new IndentPrinter(output, " " * indention, true, false)
    }

    JavaScriptCodeWriter(int indention = 4) {
        this(new StringWriter(), indention)
    }

    void enclosedBlock(Closure callback) {
        out.println('(function () {')
        callBlock(callback)
        out.println('})();')
    }

    void function(String name = null, Closure block) {
        out.println("function ${name ?: ""}() {")
        callBlock(block)
        out.println("}")
    }

    void var(String name, String value) {
        out.println("var ${name} = ${value};")
    }

    protected void callBlock(Closure block) {
        out.incrementIndent()
        out.autoIndent = true
        block()
        out.autoIndent = false
        out.decrementIndent()
    }

    void call(String funcName, List<String> params = []) {
        out.println("${funcName}(${params.join(', ')});")
    }

    void call(String on, String funcName, List<String> params = []) {
        out.println("${on}.${funcName}(${params.join(', ')});")
    }

    void assignFromCall(String varname, String funcName, List<String> params = []) {
        out.println("var ${varname} = ${funcName}(${params.join(', ')});")
    }

    void assignFromCall(String varname, String on, String funcName, List<String> params = []) {
        out.print('var')
        out.print(' ')
        out.print(varname)
        out.print(' = ')
        out.print("${on}.${funcName}")
        out.print('(')
        out.print(params.join(', '))
        out.print(');')
        out.println()
    }

    void newline() {
        out.print('\n')
    }

    @Override
    String toString() {
        return actual.toString()
    }
}
