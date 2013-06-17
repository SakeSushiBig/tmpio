package org.tmpio.fslang

class Scanner {

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
        Token next = scan()
        if(next != null) token << next
        return next
    }

    private Token scan() {
        if(position == code.size())
            return null
        def value = code.substring(position++, position)
        if(value.matches(/(\,|\;)/)) {
            return scan()
        } else if(value.matches(/(\[|\])/)) {
            return Token.sqrBracket(ParenStates.fromString(value))
        } else if(value.matches(/(\(|\))/)) {
            return Token.paren(ParenStates.fromString(value))
        } else {
            def expr = ~/[a-zA-Z0-9-_\ \.]/
            def name = code.substring(position).takeWhile {
                expr.matcher(it.toString()).matches()
            }
            position += name.value.size()
            return Token.name(value + name)
        }
    }

    def skip(int t) {
        if(t < 0)
            throw new IllegalArgumentException("Cannot skip " + t + " tokens")
        t.times { scan() }
        return this
    }

}
