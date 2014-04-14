package gutter.runtime

import groovy.transform.CompileStatic

import java.nio.file.CopyOption
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path

@CompileStatic
class GutterNIOMethods {
    static boolean exists(Path path, LinkOption... linkOptions) {
        Files.exists(path, linkOptions)
    }

    static long copyTo(Path source, Path target, CopyOption... options) {
        Files.copy(source, target, options)
    }

    static long copyFrom(Path target, Path source, CopyOption... options) {
        Files.copy(source, target, options)
    }
}
