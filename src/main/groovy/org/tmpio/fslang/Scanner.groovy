package org.tmpio.fslang

class Scanner {

    private String code
    private List<Token> token
    private int position

    def setSource(String c) {
        if(c == null)
            throw new IllegalArgumentException("fslang.Scanner cannot accept null value for code", new NullPointerException())
        this.code = c
        this.position = 0
        this.token = []
    }

    def List<Token> all() {
        while(next() != null);
        return token
    }

    def Token next() {
        if(position == code.size())
            return null
        def value = code.substring(position++, position)
        Token token
        if([",", ";"].contains(value)) {
            return next()
        } else if(["[","]"].contains(value)) {
            token = Token.sqrBracket(ParenStates.fromString(value))
        } else if(["(",")"].contains(value)) {
            token = Token.paren(ParenStates.fromString(value))
        } else {
            for(; position < code.size(); position++) {
                def n = code.substring(position, position + 1)
                if(!n.matches(/[a-zA-Z0-9-_\ \.]/)) {
                    break
                }
                value += n
            }
            token = Token.name(value)
        }
        this.token << token
        return token
    }

    def skip(int t) {
        t.times {
            next()
            token.pop()
        }
        return this
    }

}
