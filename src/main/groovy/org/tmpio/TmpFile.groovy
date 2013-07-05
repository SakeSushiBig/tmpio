package org.tmpio

import groovy.json.JsonOutput

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

    static List<String> knownFileTypes = ["json"]

    String fileType

    def persist(Object entity) {
        if(fileType == null) detectFileType()
        if(fileType == "json") {
            this << JsonOutput.toJson(entity)
        } else {
            throw new IllegalStateException("only json persistence supported yet ;/)")
        }
    }

    def detectFileType() {
        String fileName = path.fileName.toString()
        if(fileName.matches(/.*\.[a-z]+/)) {
            int dotIndex = fileName.lastIndexOf('.')
            fileType = fileName.substring(dotIndex + 1)
        } else {
            String detectedType = probeContentType(path)
            if(knownFileTypes.contains(detectedType)) {
                fileType = detectedType
            } else
                fileType = "serialize"
        }
    }

}
