package org.tmpio.fslang

enum ParenStates {
    Open, Closed;

    static fromString(CharSequence value) {
        if(["(", "["].contains(value))
            return Open
        else if([")", "]"].contains(value))
            return Closed
        throw IllegalArgumentException("Passed String cannot be recognized as parentheses: " + value)
    }
}
