package gutter.test

import gutter.MultiMap
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.JVM)
class MultiMapTest {
    @Test
    void testCreationWorks() {
        new MultiMap<String>()
    }

    @Test
    void testAddWorks() {
        def map = new MultiMap<String>()

        map.add("key1", "value1")
        map.add("key1", "value2")
        map.add("key2", "value1")
        map.add("key2", "value2")

        assert map.keys() == [ "key1", "key2" ].toSet()
        assert map["key1"] == [ "value1", "value2" ]
        assert map["key2"] == [ "value1", "value2" ]
    }

    @Test
    void testToMultiMapWorks() {
        def original = [ key1: "value", key2: "value" ]
        def map = original.toMultiMap()

        assert map["key1"] == ["value"]
        assert map["key2"] == ["value"]
    }
}
