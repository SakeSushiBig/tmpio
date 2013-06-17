package org.tmpio.fslang

enum ParenStates {
    Open, Closed;

    static fromString(CharSequence value) {
        if(value.matches(/(\(|\[)/))
            return Open
        else if(value.matches(/(\)|\])/))
            return Closed
        throw IllegalArgumentException("Passed String cannot be recognized as parentheses: " + value)
    }
}
