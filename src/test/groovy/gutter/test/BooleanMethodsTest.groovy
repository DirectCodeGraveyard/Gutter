package gutter.test

import org.junit.Test

class BooleanMethodsTest {
    @Test
    void testNegate() {
        assert false.negate()
    }

    @Test
    void testAnd() {
        assert true.and(true)
        assert (true.and(false)).negate()
    }

    @Test
    void testOr() {
        assert true.or(false)
    }

    @Test
    void testXor() {
        assert true.xor(false)
    }
}
