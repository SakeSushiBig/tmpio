package org.tmpio

import org.tmpio.fslang.FsScanner
import org.tmpio.fslang.Parser

import java.nio.file.Path

import static java.nio.file.Files.*
import static java.nio.file.Paths.*

import static org.tmpio.RecursiveRemove.*

class TmpDir {

    def parser = new Parser()
    def scanner = new FsScanner()
    private Path rootPath

    void setRoot(String value) {
        def p
        if(value == null || value == "" || !isDirectory(p = get(value)))
            throw new IllegalArgumentException("cannot set " + value + " as root path")
        rootPath = p
    }

    String getRoot() {
        rootPath.toString()
    }

    def create(String code) {
        def subTree = parseCode code
        persistStructure subTree
    }

    private parseCode(String code) {
        scanner.setSource(code)
        parser.parse(scanner)
    }

    private persistStructure(Map subTree, Path root = rootPath) {
        if(subTree.containsKey("files")) {
            subTree["files"].each { createFile(root.resolve(it)) }
            subTree.remove("files")
        }
        subTree.keySet().each {
            persistStructure subTree[it], createDirectory(root.resolve(it))
        }
    }

    def cleanup(Path root = rootPath) {
        removeRecursively root
    }
}
