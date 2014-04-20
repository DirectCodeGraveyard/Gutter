package gutter.eventbus

import groovy.transform.CompileStatic
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.SimpleType
import gutter.collections.MultiMap

/**
 * The Gutter EventBus is a simple and effective event bus which is String and Closure based.
 */
@CompileStatic
class EventBus {
    private final MultiMap<Closure<?>> handlers = new MultiMap<>()
    private final EventBusOption[] options

    EventBus(EventBusOption... options) {
        this.options = options
    }

    static EventBus create(EventBusOption... options) {
        return new EventBus(options)
    }

    void on(String eventName, @ClosureParams(value = SimpleType, options = ["Map<String, ? extends Object>"]) Closure handler) {
        handlers.add(eventName, handler)
    }

    void dispatch(String eventName, Map<String, ? extends Object> params = [:]) {
        /* If there are no handlers, check if dead events are enabled, and if so, dispatch a dead event */
        if (!handlers.contains(eventName)) {
            if (EventBusOption.EMIT_DEAD_EVENT in options && eventName != "dead.event") {
                dispatch("dead.event", [
                        eventName: eventName,
                        params: params
                ])
            }
            return
        }

        if (EventBusOption.THREAD_PER_EVENT in options) {
            Thread.startDaemon("EventExecutor[${eventName}]") { ->
                handlers[eventName].each { handler ->
                    handler(params)
                }
            }
        } else if (EventBusOption.THREAD_PER_HANDLER in options) {
            handlers[eventName].each { handler ->
                Thread.startDaemon("EventExecutor[${eventName}]") { ->
                    handler(params)
                }
            }
        } else {
            handlers[eventName].each { handler ->
                handler(params)
            }
        }
    }
}
