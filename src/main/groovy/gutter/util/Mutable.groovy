package gutter.util

import groovy.transform.CompileStatic

@CompileStatic
class Mutable<T> implements Disposable {
    private T value

    private Mutable(final T value = null) {
        this.value = value
    }

    static <T> Mutable<T> of(final T value = null) {
        return new Mutable<T>(value)
    }

    T get() {
        return value
    }

    T set(final T value) {
        final T old = value
        this.value = value
        return old
    }

    boolean asBoolean() {
        return value != null && value.asBoolean()
    }

    T call() {
        return get()
    }

    T call(final T value) {
        return set(value)
    }

    /**
     * Just in case, we can at least try to GC the reference
     */
    @Override
    void finalize() {
        dispose()
    }

    @Override
    void dispose() {
        if (value instanceof Disposable) {
            value.dispose()
        }
        value = null
    }

    @Override
    boolean equals(Object other) {
        value == other
    }
}
