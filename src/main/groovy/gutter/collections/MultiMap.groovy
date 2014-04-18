package gutter.collections

/**
 * A MultiMap is a map to a String and a List of objects
 * @param < V > Value Type
 */
class MultiMap<V> {
    private final Map<String, List<V>> delegate

    MultiMap() {
        delegate = new HashMap<String, List<V>>()
    }

    List<V> getAt(String key) {
        get(key)
    }

    void putAt(String key, V value) {
        add(key, value)
    }

    List<V> get(String key) {
        return delegate.containsKey(key) ? delegate[key] : (delegate[key] = [])
    }

    boolean contains(String key) {
        return key in keys()
    }

    void add(String key, V value) {
        get(key).add(value)
    }

    boolean empty(String key) {
        return !delegate.containsKey(key) || delegate[key].empty
    }

    Set<String> keys() {
        return delegate.keySet()
    }

    List<List<V>> values() {
        return delegate.values().toList()
    }

    void each(Closure closure) {
        delegate.each(closure)
    }

    void each(String key, Closure closure) {
        get(key).each(closure)
    }

    List<V> remove(String key) {
        return delegate.remove(key)
    }

    V last(String key) {
        return empty(key) ? null : get(key).last()
    }

    List<V> take(String key, int amount) {
        return get(key).take(amount)
    }

    void clear() {
        delegate.clear()
    }

    @Override
    String toString() {
        return delegate.toString()
    }
}