package gutter.text

import groovy.text.Template
import groovy.text.TemplateEngine
import org.codehaus.groovy.control.CompilationFailedException

/**
 * A Very Simple Brace-notation Variable Template Engine.
 * <code>
 *     def engine = new BraceVariableTemplateEngine()
 *     def template = engine.createTemplate("""\
 *          <hello>{Hi}</hello>
 *     """.stripIndent())
 *     def output = template.make([ Hi: "Hello World" ]).toString()
 *     assert output == "<hello>Hello World</hello>"
 *
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
