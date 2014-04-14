package gutter

class Mutable<T> {
    private T value

    private Mutable(T value = null) {
        this.value = value
    }

    static <T> Mutable<T> of(T value = null) {
        return new Mutable<T>(value)
    }

    T get() {
        return value
    }

    T set(T value) {
        T old = value
        this.value = value
        return old
    }

    boolean asBoolean() {
        return value != null && value.asBoolean()
    }

    T call() {
        return get()
    }

    T call(T value) {
        return set(value)
    }
}
