package org.tmpio

import org.junit.runner.RunWith
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

@RunWith(Sputnik)
class DeletingDirs extends Specification {

    def tmpDir

    def setup() {
        tmpDir = new TmpDir(root: '/tmp/tmpio/')
    }

    def "delete directory"() {
        setup:
        tmpDir.create fileTree
        when:
        tmpDir.cleanup()
        then:
        paths.every { !Files.exists(Paths.get(it)) }
        where:
        fileTree << ["folder1", "folder1;folder2"]
        paths << [["/tmp/tmpio/folder1"], ["/tmp/tmpio/folder1", "/tmp/tmpio/folder2"]]
    }

    def "delete by timer"() {

    }

}
