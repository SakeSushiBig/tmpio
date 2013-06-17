package org.tmpio.fslang

import org.junit.runner.RunWith
import org.spockframework.runtime.Sputnik
import spock.lang.Specification

import static org.tmpio.fslang.ParenStates.*
import static org.tmpio.fslang.Token.*

@RunWith(Sputnik)
class Scanning extends Specification {

    def "tokenize flat file trees"() {
        setup:
        def scanner = new Scanner()
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
    }

}
