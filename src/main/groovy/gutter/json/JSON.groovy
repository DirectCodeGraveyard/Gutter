package gutter.json

import groovy.json.JsonBuilder
import groovy.json.JsonOutput

/**
 * An easy to use class for everything JSON
 * <p>The purpose of this class is to provide an easy to use API for Groovy's amazing JSON Utilities</p>
 * @author Kenneth Endfinger
 */
class JSON {
    static Object parse(String input) {
        input.parseJSON()
    }

    static String stringify(Object input) {
        input.encodeJSON()
    }

    static String build(@DelegatesTo(JsonBuilder) Closure closure) {
        def builder = new JsonBuilder()
        builder.with(closure)
        builder.toString()
    }

    static String prettyPrint(String input) {
        JsonOutput.prettyPrint(input)
    }

    /**
     * A Wrapper around JsonSlurper and Groovy's Amazing Type Coercion
     * @param type type to create
     * @param input JSON input
     * @return instance of the specified type
     */
    static <T> T parse(Class<T> type, String input) {
        return input.parseJSON().asType(type)
    }
}
