package org.tmpio.fslang

import org.junit.runner.RunWith
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

import static org.tmpio.fslang.TokenTypes.*
import static org.tmpio.fslang.Token.*

@RunWith(Sputnik)
class Scanning extends Specification {

    FsScanner scanner

    def setup() {
        scanner = new FsScanner()
    }

    def "tokenize file trees"() {
        when:
        scanner.setSource(fileTree)
        then:
        scanner.all() == tokens
        where:
        fileTree                    | tokens
        "folder1"                   | [nameToken("folder1")]
        "folder1;folder2"           | [nameToken("folder1"), nameToken("folder2")]
        "[file1]"                   | [token(OpenSqrBracket), nameToken("file1"), token(ClosedSqrBracket) ]
        "[file1,file2]"             | [token(OpenSqrBracket), nameToken("file1"), nameToken("file2"), token(ClosedSqrBracket) ]
        "[file1]folder1;folder2"    | [token(OpenSqrBracket), nameToken("file1"), token(ClosedSqrBracket), nameToken("folder1"), nameToken("folder2") ]
        ""                          | []
        "folder1(folder12;folder21)"| [nameToken("folder1"), token(OpenParen), nameToken("folder12"), nameToken("folder21"), token(ClosedParen)]
        "[file1]folder1(folder12)"  | [token(OpenSqrBracket), nameToken("file1"), token(ClosedSqrBracket), nameToken("folder1"),
                                           token(OpenParen), nameToken("folder12"), token(ClosedParen)]
        "folder1([file1,file2]folder12(folder21))" | [nameToken("folder1"), token(OpenParen),
                token(OpenSqrBracket), nameToken("file1"), nameToken("file2"), token(ClosedSqrBracket),
                nameToken("folder12"), token(OpenParen), nameToken("folder21"), token(ClosedParen), token(ClosedParen)]
    }

    def "matching file name variations"() {
        when:
        scanner.setSource(fileTree)
        def token = scanner.skip(1).next()
        then:
        token.type == TokenTypes.Name
        token.value == fileName
        where:
        fileTree            | fileName
        "[_test-file.txt]"  | "_test-file.txt"
        "[File Name,file2]" | "File Name"
    }

}
