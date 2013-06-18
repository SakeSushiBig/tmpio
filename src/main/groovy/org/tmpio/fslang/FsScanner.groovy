package org.tmpio.fslang

import static org.tmpio.fslang.Token.*

class FsScanner {

    private String code
    private List<Token> token
    private int position

    def setSource(String c) {
        if(c == null)
            throw new IllegalArgumentException("Cannot accept null value for code")
        this.code = c
        this.position = 0
        this.token = []
    }

    def List<Token> all() {
        while(next() != null);
        return token
    }

    def Token next() {
        int pos = position
        Token next = scan()
        if(next != null) {
            next.codePosition = pos
            token << next
        }
        return next
    }

    private Token scan() {
        if(position == code.size())
            return null
        def value = code.substring(position++, position)
        if(value.matches(/(\,|\;)/)) {
            return scan()
        } else if(value.matches(/(\[|\]|\(|\))/)) {
            return token(TokenTypes.forParentheses(value))
        }  else {
            def expr = ~/[a-zA-Z0-9-_\ \.]/
            def name = code.substring(position).takeWhile {
                expr.matcher(it.toString()).matches()
            }
            position += name.size()
            return nameToken(value + name)
        }
    }

    def skip(int t) {
        if(t < 0)
            throw new IllegalArgumentException("Cannot skip " + t + " tokens")
        t.times { scan() }
        return this
    }

    def empty() {
        position >= code.size()
    }

}
