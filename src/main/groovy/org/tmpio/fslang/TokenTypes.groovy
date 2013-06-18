package org.tmpio.fslang

enum TokenTypes {
    Name, OpenSqrBracket, ClosedSqrBracket, OpenParen, ClosedParen;

    public static TokenTypes forParentheses(String symbol) {
        switch(symbol) {
            case "[": return OpenSqrBracket
            case "]": return ClosedSqrBracket
            case "(": return OpenParen
            case ")": return ClosedParen
        }
    }

}
