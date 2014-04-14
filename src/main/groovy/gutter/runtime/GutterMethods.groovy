package gutter.runtime

import gutter.Mutable

class GutterMethods {

    static <T> Mutable<T> asMutable(T value) {
        return Mutable.of(value)
    }

    static <T> List<T> reverse(List<T> that) {
        def newList = []
        that.reverseEach { value ->
            newList.add(value)
        }
        return newList
    }
}
