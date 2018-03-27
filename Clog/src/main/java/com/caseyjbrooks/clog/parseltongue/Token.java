package com.caseyjbrooks.clog.parseltongue;

class Token {
    enum Type {
        ANY,

        CLOG_START,
        CLOG_SIMPLE,

        PIPE,
        HASH,
        LPAREN,
        RPAREN,
        LCURLYBRACE,
        RCURLYBRACE,
        LBRACKET,
        RBRACKET,
        COMMA,
        DOT,
        AT,
        DOLLARSIGN,

        NUMBER,
        QUOTE,

        WORD,
        STRING,
    }

    private Type type;
    private String stringValue;
    private int intValue;
    private double doubleValue;
    private boolean booleanValue;

    Token(Type type) {
        this.type = type;
    }

    Token(Type type, String value) {
        this.type = type;
        this.stringValue = value;
    }

    Token(Type type, int value) {
        this.type = type;
        this.intValue = value;
        this.stringValue = Integer.toString(value);
    }

    String getStringValue() { return stringValue; }

    int getIntValue() { return intValue; }

    boolean equals(Type type) { return this.type == type; }

    public Type getType() {
        return type;
    }
}
