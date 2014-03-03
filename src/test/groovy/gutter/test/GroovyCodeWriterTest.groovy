package gutter.test

import groovy.xml.MarkupBuilder
import gutter.util.GroovyCodeWriter
import org.junit.Test

class GroovyCodeWriterTest {
    @Test
    void testBasicPrintln() {
        assertCodeWriterScript {
            createMethodCall(null, "println", "'Hello World'")
        }
    }

    @Test
    void testMarkupBuilder() {
        assertCodeWriterScript { ->
            createImport(MarkupBuilder.class.name)
            createVariableDeclaration(null, "output", "new StringWriter()")
            createVariableDeclaration(null, "builder", "new MarkupBuilder(output)")
            createSimpleClosureCall("builder", "html") { ->
                createSimpleClosureCall("head") { ->
                    createMethodCall(null, "title", '"Hello"')
                }
            }
            createMethodCall(null, "println", 'output')
        }
    }

    static void assertCodeWriterScript(@DelegatesTo(GroovyCodeWriter) Closure closure) {
        def out = new StringWriter()
        def w = new GroovyCodeWriter(out, 4)
        closure.delegate = w
        closure()
        def shell = new GroovyShell()
        def script = shell.parse(out.toString())
        println "-------------------------------------------------------------"
        print out.toString()
        println "-------------------------------------------------------------"
        script.run()
    }
}
