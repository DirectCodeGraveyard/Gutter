package gutter.util

/**
 * Gives a way to dispose of an object
 * <p>Note: This does not GC it, but it can be if necessary</p>
 */
interface Disposable {
    void dispose()
}
