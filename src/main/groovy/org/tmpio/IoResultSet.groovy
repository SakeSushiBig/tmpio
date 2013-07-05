package org.tmpio

import java.nio.charset.Charset
import java.nio.file.Path

import static java.nio.file.Files.*

class IoResultSet {

    List<Path> paths

    def IoResultSet(List<Path> paths) {
        this.paths = paths
    }

    def leftShift(List<String> contents) {
        this.paths.eachWithIndex { Path entry, int i ->
            write(entry, contents[i].bytes)
        }
    }

    def leftShift(Closure<String> contentCollector) {
        this.paths.each {
            write(it, contentCollector(it).bytes)
        }
    }

}
