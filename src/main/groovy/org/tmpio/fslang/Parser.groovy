package org.tmpio.fslang

import static org.tmpio.fslang.TokenTypes.*
import static org.tmpio.fslang.ParserEx.*

class Parser {

    def parse(FsScanner scanner) {
        def ast = [:]
        def lastDirectory = null
        while(!scanner.empty()) {
            def token = scanner.next()
            switch(token.type) {
                case Name:
                    if(ast.keySet().contains(token.value))
                        throw new ParserEx(type: DIR_TYPE, duplicate: token.value)
                    ast[lastDirectory = token.value] = [:]
                    break;
                case OpenSqrBracket:
                    ast["files"] = []
                    while(!scanner.empty() && (token = scanner.next()).type == Name) {
                        if(ast["files"].contains(token.value))
                            throw new ParserEx(type: FILE_TYPE, duplicate: token.value)
                        ast["files"] << token.value
                    }
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
