package gutter.test

import gutter.eventbus.EventBus
import gutter.eventbus.EventBusOption
import org.junit.Test

class EventBusTest {
    @Test
    void testEventBusNotThreadedWorks() {
        def bus = new EventBus()
        def theStuff = ""
        bus.on("test") { opts ->
            theStuff = opts.payload
        }
        bus.dispatch("test", [ payload: "Test" ])
        assert theStuff == "Test"
    }

    @Test
    void testEventBusThreadPerEventWorks() {
        def bus = new EventBus(EventBusOption.THREAD_PER_EVENT)
        def theStuff = ""
        bus.on("test") { opts ->
            theStuff = opts.payload
        }
        bus.dispatch("test", [ payload: "Test" ])
        sleep(200)
        assert theStuff == "Test"
    }

    @Test
    void testEventBusThreadPerHandlerWorks() {
        def bus = new EventBus(EventBusOption.THREAD_PER_HANDLER)
        def theStuff = ""
        bus.on("test") { opts ->
            theStuff = opts.payload
        }
        bus.dispatch("test", [ payload: "Test" ])
        sleep(200)
        assert theStuff == "Test"
    }

    @Test
    void testEventBusEmitDeadEventWorks() {
        def bus = new EventBus(EventBusOption.EMIT_DEAD_EVENT)
        def theStuff = ""
        bus.on("dead.event") { opts ->
            assert opts.eventName == "test"
            theStuff = opts.params.payload
        }
        bus.dispatch("test", [ payload: "Test" ])
        assert theStuff == "Test"
    }
}
