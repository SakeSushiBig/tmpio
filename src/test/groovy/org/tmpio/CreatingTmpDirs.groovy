package org.tmpio

import org.junit.runner.RunWith
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

@RunWith(Sputnik)
class CreatingTmpDirs extends Specification {
    def tmpFolder

    def setup() {
        tmpFolder = new TmpDir(root: "/tmp/")
    }

    def cleanup() {
        tmpFolder.cleanup()
    }

   /* def "only accepts existing root paths"() {
        when:
        def dir = new TmpDir(root: path)
        then:
        if(accepts)
            dir.root == path
        else
            thrown(IllegalArgumentException)
        where:
        path                                | accepts
        "/tmp/"                             | true
        "/"                                 | true
        System.properties["user.home"]      | true
        System.properties["java.io.tmpdir"] | true
        "invalid directory path"            | false
        "(!/§"                              | false
        null                                | false

    }   */

    def "can create directories at all"() {
        when:
        tmpFolder.create "folder1"
        then:
        Files.isDirectory(Paths.get("/tmp/folder1/"))
    }

    def "create temporary directories with string notation"() {
        when:
        tmpFolder.create fileTree
        then:
        paths.every { Files.isDirectory(Paths.get(it)) }
        where:
        fileTree << ["folder1", "folder1;folder2"]
        paths << [["/tmp/folder1"], ["/tmp/folder1", "/tmp/folder2"]]
    }

    def "create subdirectories"() {
        when:
        tmpFolder.create fileTree
        then:
        paths.every { Files.isDirectory(Paths.get(it)) }
        where:
        fileTree                                | paths
        "folder1(folder1_1)"                    | ["/tmp/folder1/folder1_1"]
        "folder1(folder1_1;folder1_2)"          | ["/tmp/folder1/folder1_1","/tmp/folder1/folder1_2"]
        "folder1(folder1_1);folder2(folder2_1)" | ["/tmp/folder1/folder1_1","/tmp/folder2/folder2_1"]
        "folder1(folder1_1(folder1_1_1))"       | ["/tmp/folder1/folder1_1/folder1_1_1"]
    }
     /*
    def "fail on duplicated directory name"() {
        when:
        tmpFolder.create fileTree
        then:
        DuplDirEx ex = thrown()
        ex.dirname == duplicate
        paths.every { !Files.isDirectory(Paths.get(it)) }
        where:
        fileTree                            | paths                                     | duplicate
        "folder1;folder1"                   | ["/tmp/folder1/"]                         | "folder1"
        "folder1;folder2;folder2"           | ["/tmp/folder1","/tmp/folder2"]           | "folder2"
        "folder1(folder1_2;folder1_2)"      | ["/tmp/folder1","/tmp/folder1/folder1_2"] | "folder1_2"
        "folder1;folder1;folder2;folder2"   | ["/tmp/folder1","/tmp/folder2"]           | "folder1"
    }              */
}
