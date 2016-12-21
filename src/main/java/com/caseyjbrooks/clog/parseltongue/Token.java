package com.caseyjbrooks.clog.parseltongue;

public class Token {
    public enum Type {
        ANY,

        CLOG_START,

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

    public String getStringValue() { return stringValue; }

    public int getIntValue() { return intValue; }

    public boolean equals(Type type) { return this.type == type; }
}
