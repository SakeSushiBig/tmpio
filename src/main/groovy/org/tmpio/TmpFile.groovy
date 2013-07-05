package org.tmpio

import java.nio.file.Path

import static java.nio.file.Files.*

class TmpFile {

    private Path path

    def TmpFile(Path path) {
        if(path == null || !isRegularFile(path))
            throw new IllegalArgumentException("TmpFile requires a available file path")
        this.path = path
    }

    def getPath() {
        return path
    }

    def leftShift(String content) {
        if(content == null)
            throw new IllegalArgumentException("Cannot write null-string to file")
        write(path, content.bytes)
    }

}
