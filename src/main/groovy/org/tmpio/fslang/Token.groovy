package org.tmpio.fslang

class Token {
    static Token name(String value) {
        new Token(type: TokenTypes.Name, value: value)
    }

    static Token sqrBracket(ParenStates state) {
        def token = new Token(type: TokenTypes.SquareBracket, value: state)
        token.attributes.put("state", state)
        return token
    }

    TokenTypes type
    String value
    def attributes = [:]

    @Override
    def String toString() {
        return "(" + value + ": " + type + ")";
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Token token = (Token) o

        if (attributes != token.attributes) return false
        if (type != token.type) return false
        if (value != token.value) return false

        return true
    }

    int hashCode() {
        int result
        result = type.hashCode()
        result = 31 * result + (value != null ? value.hashCode() : 0)
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0)
        return result
    }
}
