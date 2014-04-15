package gutter.util

import groovy.transform.CompileStatic

/**
 * Gives a way to dispose of an object
 * <p>Note: This does not GC it, but it can be if necessary</p>
 */
@CompileStatic
interface Disposable {
    /**
     * Disposes the Object
     */
    void dispose()
}
