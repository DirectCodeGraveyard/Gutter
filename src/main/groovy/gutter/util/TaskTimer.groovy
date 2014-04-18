package gutter.util

import groovy.transform.CompileStatic

@CompileStatic
class TaskTimer {
    static long timeOf(Closure closure) {
        def startTime = System.currentTimeMillis()
        closure()
        def stopTime = System.currentTimeMillis()
        return stopTime - startTime
    }
}
