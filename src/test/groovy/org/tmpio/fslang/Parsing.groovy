package org.tmpio.fslang

import org.junit.runner.RunWith
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

import static org.tmpio.fslang.ParserEx.*

@RunWith(Sputnik)
class Parsing extends Specification {

    def parser
    def scanner

    def setup() {
        parser = new Parser()
        scanner = new FsScanner()
    }

    def "create directory ASTs"() {
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

    def "do not allow duplicated directory or file names"()  {
        when:
        scanner.setSource(fileTree)
        parser.parse(scanner)
        then:
        ParserEx e = thrown()
        e.duplicate == duplicate
        e.type == type
        where:
        fileTree << ["f;f", "f;b;f", "f(f;f)", "[d,s,d,f]f", "f([d,d])"]
        duplicate << ["f", "f", "f", "d", "d"]
        type << [DIR_TYPE, DIR_TYPE, DIR_TYPE, FILE_TYPE, FILE_TYPE]
    }

}
