package org.tmpio

import org.tmpio.fslang.FsScanner
import org.tmpio.fslang.Parser

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.tmpio.CleanupStrategy.*

class TmpDir {

    def parser = new Parser()
    def scanner = new FsScanner()
    private Path root
    def fileTree = [:]
    def CleanupStrategy strategy
    private Thread cleanupHook = null
    private Timer timer = new Timer()
    private TimerTask timerTask = null

    void setRoot(String value) {
        if(value == null || !Files.isDirectory(root = Paths.get(value)))
            throw new IllegalArgumentException(sprintf("cannot set %s as root path", value))
    }

    String getRoot() {
        root.toString()
    }

    void setStrategy(CleanupStrategy newStrategy, lifeTime = null) {
        def oldStrategy = this.strategy
        if(oldStrategy == OnExit) {
            Runtime.getRuntime().removeShutdownHook cleanupHook
            cleanupHook = null
        }
        if(oldStrategy == Timestamp) {
            timerTask.cancel()
            timerTask = null
        }
        if(newStrategy == OnExit) {
            cleanupHook = new Thread({ cleanup() })
            Runtime.getRuntime().addShutdownHook cleanupHook
        }
        if(newStrategy == Timestamp) {
            timerTask = (timer.runAfter(lifeTime, { cleanup() }))
        }
        this.strategy = newStrategy
    }

    def create(String code) {
        def subTree = parseCode code
        def structure = persistStructure subTree
        fileTree += structure
    }

    def parseCode(String code) {
        scanner.setSource(code)
        parser.parse(scanner)
    }

    def persistStructure(Map subTree, rootPath = root.toString() + "/") {
        def persistedTree = [:]
        if(subTree.containsKey("files")) {
            persistedTree["files"] = []
            subTree["files"].each { Files.createFile(persistedTree["files"] << Paths.get(rootPath + it)) }
        }
        subTree.remove("files")
        subTree.keySet().each {
            def path = Paths.get(rootPath + it)
            Files.createDirectory(path)
            persistedTree.put path, persistStructure(subTree[it], rootPath + "$it/")
        }
        return persistedTree
    }

    def cleanup(fileTree = this.fileTree) {
        fileTree.keySet().each {
            if(it.toString() == "files")
                fileTree[it].each { Files.deleteIfExists(it) }
            cleanup(fileTree[it])
            Files.deleteIfExists(it)
        }
    }

    void finalize() {
        if(this.strategy == ByGc) {
            cleanup()
        }
    }
}
