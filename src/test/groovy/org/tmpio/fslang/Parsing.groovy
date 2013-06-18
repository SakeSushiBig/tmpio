package org.tmpio.fslang

import org.junit.runner.RunWith
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

@RunWith(Sputnik)
class Parsing extends Specification {

    def "create directory ASTs"() {
        setup:
        def parser = new Parser()
        def scanner = new FsScanner()
        when:
        scanner.setSource(fileTree)
        def ast = parser.parse(scanner)
        then:
        ast == expectedAst
        where:
        fileTree            | expectedAst
        "folder1"           | [folder1: [:]]
        "folder1;folder2"   | [folder1:[:], folder2:[:]]
        ""                  | [:]
        "[file1]folder1"    | [files: ["file1"], folder1: [:]]
        "folder1(folder11)" | [folder1: [folder11:[:]]]
        "folder1([file1])"  | [folder1: [files: ["file1"]]]
        "[file1]folder1(folder11;folder12);folder2([file21,file22]folder21)" |
              [ files: ["file1"], folder1: [folder11:[:], folder12:[:]], folder2: [files:["file21", "file22"], folder21:[:]]]
    }

}
