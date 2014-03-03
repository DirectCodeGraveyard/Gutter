package gutter.test

import gutter.util.JavaScriptCodeWriter

class JavaScriptCodeWriterTest {
    static void main(String[] args) {
        def code = new JavaScriptCodeWriter()
        code.assignFromCall('fs', 'require', ["'fs'"])
        code.newline()
        code.function('main') {
            code.call('console', 'log', ["'test'"])
        }
        code.newline()
        code.call('main')
        print code.toString()
    }
}
