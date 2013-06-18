package org.tmpio.fslang

import static org.tmpio.fslang.TokenTypes.*

class Parser {

    def parse(FsScanner scanner) {
        def ast = [:]
        def lastDirectory = null
        while(!scanner.empty()) {
            def token = scanner.next()
            switch(token.type) {
                case Name:
                    ast[lastDirectory = token.value] = [:]
                    break;
                case OpenSqrBracket:
                    ast["files"] = []
                    while(!scanner.empty() && (token = scanner.next()).type == Name)
                        ast["files"] << token.value
                    break;
                case OpenParen:
                    ast[lastDirectory] = parse(scanner)
                    break;
                case ClosedParen:
                    return ast
            }
        }
        return ast;
    }

}
