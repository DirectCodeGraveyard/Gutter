package gutter.text

import groovy.text.Template
import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration

class GroovyTemplates {
    static Template createMarkupTemplate(Reader reader) {
        def config = new TemplateConfiguration()
        config.autoNewLine = true
        config.autoIndent = true
        config.useDoubleQuotes = true
        def engine = new MarkupTemplateEngine(config)
        return engine.createTemplate(reader)
    }
}
