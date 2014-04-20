package gutter.runtime

import groovy.json.JsonParserType
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic

import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path

@CompileStatic
class GutterIOMethods {
    static boolean exists(Path path, LinkOption... linkOptions) {
        Files.exists(path, linkOptions)
    }

    static void copyTo(Path source, Path target, CopyOption... options) {
        Files.copy(source, target, options)
    }

    static void copyFrom(Path target, Path source, CopyOption... options) {
        Files.copy(source, target, options)
    }

    static Object parseJSON(Path source) {
        new JsonSlurper().parse(source.newReader())
    }

    static <T> T parseJSON(Path source, Class<T> type) {
        source.text.parseJSON(type)
    }

    static Object parseJSON(Path source, JsonParserType parser) {
        def slurper = new JsonSlurper()
        slurper.type = parser
        slurper.parse(source.newReader())
    }
}
