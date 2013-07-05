package org.tmpio

import groovy.json.JsonSlurper
import org.junit.runner.RunWith
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

import java.nio.file.Path

import static java.nio.file.Files.*
import static java.nio.file.Paths.*

import static java.nio.file.Files.*
import static java.nio.file.Paths.*

@RunWith(Sputnik)
class SupplyFileContent extends Specification {

    TmpDir dir

    def setup() {
       dir = new TmpDir(root: System.properties["java.io.tmpdir"] + "/tmpio/")
    }

    def cleanup() {
        dir.cleanup()
    }

    def "supply string to file"() {
        setup:
        dir.create "[test.txt]"
        when:
        dir["test.txt"] << "Hello File"
        then:
        new String(readAllBytes(dir.rootPath.resolve("test.txt"))) == "Hello File"
    }

    def "do not accept null arguments"() {
        setup:
        dir.create "[test.txt]"
        when:
        dir["test.txt"] << null
        then:
        thrown(IllegalArgumentException)
    }

    def "write strings to multiple files"() {
        setup:
        dir.create "[a.txt,b.txt,c.csv]"
        when:
        dir.find(/.*\.txt/) << ["ab", "ab"]
        then:
        checkFileContains("a.txt", "ab")
        checkFileContains("b.txt", "ab")
        checkFileContains("c.csv", "")
    }

    def "use closure to generate file content"() {
        setup:
        dir.create "[a.txt,b.txt]"
        when:
        dir.find(/.*\.txt/) << {
            String fileName = it.fileName.toString().substring(0, 1)
            return fileName
        }
        then:
        checkFileContains("a.txt", "a")
        checkFileContains("b.txt", "b")
    }

    def "create files with content"() {
        when:
        def i = 0
        dir.createFiles(10) {
            def val = it + 1
            [sprintf("%d.log", val), val.toString()]
        }
        then:
        dir.find(/.*.log/).paths.collect {
            (new String(readAllBytes(it))).toInteger()
        }.sum() == 55 // 1 + 2 + ... + 10 == 55
    }

    def "persist object to json file"() {
        setup:
        dir.create "[raphael.json]"
        def raphi = new Buddy(name: "Raphael", salary: 292, kittenNames: ["Puppy", "Agnes", "Irmi"])
        JsonSlurper slurper = new JsonSlurper()
        when:
        dir["raphael.json"].persist(raphi)
        def parsedRaphi = slurper.parseText(new String(readAllBytes(dir.rootPath.resolve("raphael.json"))))
        then:
        raphi.name == parsedRaphi.name
        raphi.salary == parsedRaphi.salary
        raphi.kittenNames == parsedRaphi.kittenNames
    }

    def checkFileContains(String path, String content) {
        def actualContent = new String(readAllBytes(dir.rootPath.resolve(path)))
        return content == actualContent
    }

}
