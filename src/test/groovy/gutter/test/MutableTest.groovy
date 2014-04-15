package gutter.test

import org.junit.Test

class MutableTest {
    @Test
    void testReferenceWorks() {
        def m = "Hello".asMutable()
        assert m() == "Hello"
    }

    @Test
    void testAsBooleanWorks() {
        def m = "Hello".asMutable()
        assert m
        m(null)
        assert !m
    }

    @Test
    void testEqualsWorks() {
        def m = "Hello".asMutable()
        assert m == "Hello"
        m("Go")
        assert m == "Go"
    }

    @Test
    void testCallOverloadingWorks() {
        def m = "Hello".asMutable()
        assert m() == "Hello"
        m("Go")
        assert m() == "Go"
    }
}
