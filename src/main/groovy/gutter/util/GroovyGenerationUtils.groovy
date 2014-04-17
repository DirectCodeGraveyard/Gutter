package gutter.util

import groovy.transform.CompileStatic

@CompileStatic
class GroovyGenerationUtils {

    static final List<String> RESERVED_WORDS = [
            "for",
            "if",
            "in",
            "as",
            "goto",
            "const",
            "final",
            "int",
            "long",
            "short",
            "static",
            "String",
            "void",
            "transient",
            "class",
            "interface",
            "@interface",
            "trait",
            "char",
            "def",
            "true",
            "false",
            "null",
            "return"
    ]

    static String addQuotes(String input) {
        def out = new StringBuilder(input)
        out.insert(0, "'")
        out.append("'")
        return out.toString()
    }

    static String addQuotesIfNeeded(String input) {
        if (input in RESERVED_WORDS || "-" in input || " " in input) {
            return addQuotes(input)
        } else {
            return input
        }
    }
}
