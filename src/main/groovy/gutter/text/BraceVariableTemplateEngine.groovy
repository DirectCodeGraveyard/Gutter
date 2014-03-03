package gutter.text

import groovy.text.Template
import groovy.text.TemplateEngine
import org.codehaus.groovy.control.CompilationFailedException

/**
 * A Very Simple Brace-notation Variable Template Engine.
 * <code>
 * def engine = new BraceVariableTemplateEngine()<br/>
 * def template = engine.createTemplate("""\<br/>
 *    <greet>{Greeting}</greet><br/>
 * """.stripIndent())<br/>
 * assert template.make(Greeting: "Hello World").toString() == "<greet>Hello World</greet>\n"<br/>
 * </code>
 */
class BraceVariableTemplateEngine extends TemplateEngine {
    @Override
    Template createTemplate(Reader reader) throws CompilationFailedException, ClassNotFoundException, IOException {
        def input = reader.text
        def template = new Template() {
            @Override
            Writable make() {
                return new Writable() {
                    @Override
                    Writer writeTo(Writer out) throws IOException {
                        out.write(input)
                        return out
                    }

                    @Override
                    String toString() {
                        writeTo(new StringWriter()).toString()
                    }
                }
            }

            @Override
            Writable make(Map binding) {
                return new Writable() {
                    @Override
                    Writer writeTo(Writer out) throws IOException {
                        def output = input
                        for (entry in binding) {
                            output = output.replaceAll("\\{${entry.key}\\}", entry.value.toString())
                        }
                        out.write(output)
                        return out
                    }

                    @Override
                    String toString() {
                        writeTo(new StringWriter()).toString()
                    }
                }
            }
        }
        template
    }
}
