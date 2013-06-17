package org.tmpio.fslang

import org.junit.runner.RunWith
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

import static org.tmpio.fslang.ParenStates.*
import static org.tmpio.fslang.Token.*

@RunWith(Sputnik)
class Scanning extends Specification {

    Scanner scanner

    def setup() {
        scanner = new Scanner()
    }

    def "tokenize file trees"() {
        when:
        scanner.setSource(fileTree)
        then:
        scanner.all() == tokens
        where:
        fileTree                    | tokens
        "folder1"                   | [name("folder1")]
        "folder1;folder2"           | [name("folder1"), name("folder2")]
        "[file1]"                   | [sqrBracket(Open), name("file1"), sqrBracket(Closed) ]
        "[file1,file2]"             | [sqrBracket(Open), name("file1"), name("file2"), sqrBracket(Closed) ]
        "[file1]folder1;folder2"    | [sqrBracket(Open), name("file1"), sqrBracket(Closed), name("folder1"), name("folder2") ]
        ""                          | []
        "folder1(folder12;folder21)"| [name("folder1"), paren(Open), name("folder12"), name("folder21"), paren(Closed)]
        "[file1]folder1(folder12)"  | [sqrBracket(Open), name("file1"), sqrBracket(Closed), name("folder1"),
                                           paren(Open), name("folder12"), paren(Closed)]
        "folder1([file1,file2]folder12(folder21))" | [name("folder1"), paren(Open),
                sqrBracket(Open), name("file1"), name("file2"), sqrBracket(Closed),
                name("folder12"), paren(Open), name("folder21"), paren(Closed), paren(Closed)]
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
