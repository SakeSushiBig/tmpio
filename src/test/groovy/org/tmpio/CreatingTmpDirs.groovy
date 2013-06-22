package org.tmpio

import org.junit.runner.RunWith
import org.spockframework.runtime.Sputnik
import org.tmpio.fslang.ParserEx
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

@RunWith(Sputnik)
class CreatingTmpDirs extends Specification {

    static testDir = Paths.get("/tmp/tmpio")

    def setupSpec() {
        if(!Files.exists(testDir))
            Files.createDirectory(testDir)
    }

    def tmpFolder

    def setup() {
        tmpFolder = new TmpDir(root: "/tmp/tmpio/")
    }

    def cleanup() {
        tmpFolder.cleanup()
    }

    def "only accepts existing root paths"() {
        when:
        def dir = new TmpDir(root: path)
        then:
        dir.root == path
        where:
        path << ["/tmp", "/", System.properties["user.home"], System.properties["java.io.tmpdir"] ]
    }

    def "does not accept invalid root paths"() {
        when:
        def dir = new TmpDir(root: path)
        then:
        thrown(IllegalArgumentException)
        where:
        path << [null, "", "invalid folder name /(%&", "/notExistingFolder"]
    }

    def "can create directories at all"() {
        when:
        tmpFolder.create "folder1"
        then:
        Files.isDirectory(Paths.get("/tmp/tmpio/folder1/"))
    }

    def "create temporary directories with string notation"() {
        when:
        tmpFolder.create fileTree
        then:
        paths.every { Files.isDirectory(Paths.get(it)) }
        where:
        fileTree << ["folder1", "folder1;folder2"]
        paths << [["/tmp/tmpio/folder1"], ["/tmp/tmpio/folder1", "/tmp/tmpio/folder2"]]
    }

    def "create subdirectories"() {
        when:
        tmpFolder.create fileTree
        then:
        paths.every { Files.isDirectory(Paths.get(it)) }
        where:
        fileTree                                | paths
        "folder1(folder1_1)"                    | ["/tmp/tmpio/folder1/folder1_1"]
        "folder1(folder1_1;folder1_2)"          | ["/tmp/tmpio/folder1/folder1_1","/tmp/tmpio/folder1/folder1_2"]
        "folder1(folder1_1);folder2(folder2_1)" | ["/tmp/tmpio/folder1/folder1_1","/tmp/tmpio/folder2/folder2_1"]
        "folder1(folder1_1(folder1_1_1))"       | ["/tmp/tmpio/folder1/folder1_1/folder1_1_1"]
    }

    def "fail on duplicated directory name"() {
        when:
        tmpFolder.create fileTree
        then:
        ParserEx ex = thrown()
        ex.duplicate == duplicate
        ex.type == ParserEx.DIR_TYPE
        paths.every { !Files.isDirectory(Paths.get(it)) }
        where:
        fileTree                            | paths                                     | duplicate
        "folder1;folder1"                   | ["/tmp/tmpio/folder1/"]                         | "folder1"
        "folder1;folder2;folder2"           | ["/tmp/tmpio/folder1","/tmp/tmpio/folder2"]           | "folder2"
        "folder1(folder1_2;folder1_2)"      | ["/tmp/tmpio/folder1","/tmp/tmpio/folder1/folder1_2"] | "folder1_2"
        "folder1;folder1;folder2;folder2"   | ["/tmp/tmpio/folder1","/tmp/tmpio/folder2"]           | "folder1"
    }
}
