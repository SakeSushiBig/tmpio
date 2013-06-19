package org.tmpio

import org.junit.runner.RunWith
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

@RunWith(Sputnik)
class CreatingTmpFiles extends Specification {

    def TmpDir tmpdir

    def setup() {
        tmpdir = new TmpDir(root: '/tmp')
    }

    def cleanup() {
        tmpdir.cleanup()
    }

    def "can create files at all"() {
        when:
        tmpdir.create "[f]"
        then:
        Files.isRegularFile(Paths.get('/tmp/f'))
    }

    def "can create files in various combinations"() {
        when:
        tmpdir.create fileTree
        then:
        paths.every { Files.isRegularFile(Paths.get(it)) }
        where:
        fileTree            | paths
        "[f1,f2]"           | ["/tmp/f1", "/tmp/f2"]
        "[f]dir([f1,f2])"   | ["/tmp/f", "/tmp/dir/f1", "/tmp/dir/f2"]
    }

}
