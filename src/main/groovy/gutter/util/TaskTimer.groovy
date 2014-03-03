package gutter.util

import groovy.transform.CompileStatic

import java.time.Duration

@CompileStatic
class TaskTimer {
    static long timeOf(Closure closure) {
        def startTime = System.currentTimeMillis()
        closure()
        def stopTime = System.currentTimeMillis()
        return stopTime - startTime
    }

    static Duration durationOf(Closure closure) {
        return Duration.ofMillis(timeOf(closure))
    }
}
