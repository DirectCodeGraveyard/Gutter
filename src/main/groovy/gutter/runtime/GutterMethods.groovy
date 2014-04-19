package gutter.runtime

import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import gutter.collections.MultiMap
import gutter.util.Mutable

@CompileStatic
class GutterMethods {

    static <T> Mutable<T> asMutable(T value) {
        return Mutable.of(value)
    }

    static <V> MultiMap<V> toMultiMap(Map<String, V> that) {
        def newMap = new MultiMap<V>()
        that.each { key, value ->
            newMap.add(key, value)
        }
        return newMap
    }

    static <T> List<T> reverse(List<T> that) {
        def newList = []
        that.reverseEach { value ->
            newList.add(value)
        }
        return newList
    }

    static Object parseJSON(String input) {
        return new JsonSlurper().parseText(input)
    }
}
