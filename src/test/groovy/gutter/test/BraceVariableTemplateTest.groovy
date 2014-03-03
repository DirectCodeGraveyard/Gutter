package gutter.test

import gutter.text.BraceVariableTemplateEngine
import org.junit.Test

class BraceVariableTemplateTest {
    @Test
    void testNotationWorks() {
        def engine = new BraceVariableTemplateEngine()
        def template = engine.createTemplate("""\
            <greet>{Greeting}</greet>
        """.stripIndent())
        assert template.make(Greeting: "Hello World").toString() == "<greet>Hello World</greet>\n"
    }
}
