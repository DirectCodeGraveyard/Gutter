package gutter.test

import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration
import gutter.xml.Xml2GroovyMarkup
import org.junit.Ignore
import org.junit.Test

@Ignore("Currently Broken")
class Xml2GroovyMarkupTest {
    @Test
    void testElementParserWorks() {
        def writer = new StringWriter()
        def converter = new Xml2GroovyMarkup(writer)

        def input = """\
            <requests>
                <request>
                    <from>Person A</from>
                    <for>More Time to Check-in</for>
                    <processed>yes</processed>
                </request>
                <request>
                    <from>Person B</from>
                    <for>Room Service</for>
                    <processed>no</processed>
                </request>
            </requests>
        """.stripIndent()
        converter.feed(input)
        converter.complete()
        def output = writer.toString()
        println output
        assertGroovyScriptEqualsXML(output, input)
    }

    static void assertGroovyScriptEqualsXML(String script, String input) {
        def theOut = toXML(script)
        def theIn = input
        assert theIn == theOut
    }

    static String toXML(String script) {
        def config = new TemplateConfiguration()
        config.useDoubleQuotes = true
        config.autoIndent = true
        config.autoIndentString = "    "
        config.autoNewLine = true
        def templateEngine = new MarkupTemplateEngine(config)
        templateEngine.createTemplate(script).make().writeTo(new StringWriter()).toString()
    }

    static void main(String... args) {
        new Xml2GroovyMarkupTest().testElementParserWorks()
    }
}
