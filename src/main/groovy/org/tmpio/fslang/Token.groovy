package org.tmpio.fslang

class Token {

    static Token nameToken(String value) {
        new Token(type: TokenTypes.Name, value: value)
    }

    static Token token(TokenTypes type) {
        new Token(type: type)
    }

    TokenTypes type
    String value
    def attributes = [:]
    def codePosition

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
