package com.caseyjbrooks.clog.parsers;

import android.util.Log;

import java.util.LinkedList;
import java.util.Stack;

public class TokenStream {
    LinkedList<Character> chars;
    Stack<Token> ungetTokens;
    int column;

    public TokenStream(String expression) {
        chars = new LinkedList<>();
        for(int i = 0; i < expression.length(); i++) {
            chars.add(expression.charAt(i));
        }
        ungetTokens = new Stack<>();
    }

    public Token get() {
        try {
            if(ungetTokens.size() > 0) {
                return ungetTokens.pop();
            }
            else if(chars.size() > 0) {
                char ch = chars.removeFirst();
                column++;
                String s;

                switch(ch) {
                    case ' ':
                        return get();
                    case '|':
                        return new Token(Token.Type.PIPE, "|");
                    case '.':
                        return new Token(Token.Type.DOT, ".");
                    case '@':
                        return new Token(Token.Type.AT, "@");
                    case '$':
                        return new Token(Token.Type.DOLLARSIGN, "$");
                    case '\'':
                        return new Token(Token.Type.QUOTE, "'");
                    case ',':
                        return new Token(Token.Type.COMMA, ",");
                    case '#':
                        if(chars.peekFirst() == '{') {
                            chars.removeFirst();
                            column++;
                            return new Token(Token.Type.CLOG_START, "#{");
                        }
                        else {
                            return new Token(Token.Type.HASH, "#");
                        }
                    case '{':
                        return new Token(Token.Type.LCURLYBRACE, "{");
                    case '}':
                        return new Token(Token.Type.RCURLYBRACE, "}");
                    case '[':
                        return new Token(Token.Type.LBRACKET, "[");
                    case ']':
                        return new Token(Token.Type.RBRACKET, "]");
                    case '(':
                        return new Token(Token.Type.LPAREN, "(");
                    case ')':
                        return new Token(Token.Type.RPAREN, ")");
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        s = "";
                        s += ch;
                        while(chars.size() > 0 && chars.getFirst() != null &&
                                Character.isDigit(chars.getFirst())) {
                            s += chars.removeFirst();
                            column++;
                        }
                        return new Token(Token.Type.NUMBER, Integer.parseInt(s));
                    default:
                        s = "";
                        s += ch;
                        while(chars.size() > 0 && chars.getFirst() != null &&
                                Character.isLetter(chars.getFirst())) {
                            s += chars.removeFirst();
                            column++;
                        }

                        //we have our word, but we need to ensure we have removed all
                        // nonword characters that may have gotten through the above lexing cases
                        s = s.replaceAll("\\W", "");

                        return new Token(Token.Type.WORD, s);
                }
            }
            else {
                return null;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Token getAny() {
        String s = "";
        if(ungetTokens.size() > 0) {
            while(ungetTokens.size() > 0) {
                Token token = ungetTokens.pop();
                if(token != null) {
                    Log.i("TokenStream", "unget token: '" + token.getStringValue() + "'");
                    s += token.getStringValue();
                }
            }
        }

        if(chars.size() > 0) {
            char ch;

            while(chars.size() > 0 && chars.peekFirst() != null) {
                ch = chars.removeFirst();
                column++;

                Log.i("TokenStream", "char: '" + ch + "'");

                if(ch == '#' && chars.peekFirst() == '{') {
                    chars.removeFirst();
                    column++;
                    ungetTokens.push(new Token(Token.Type.CLOG_START));
                    return new Token(Token.Type.ANY, s);
                }
                else {
                    s += ch;
                }
            }

            return new Token(Token.Type.ANY, s);
        }
        else {
            return new Token(Token.Type.ANY, "");
        }
    }

    public Token getString() {
        String s = "";
        if(ungetTokens.size() > 0) {
            while(ungetTokens.size() > 0) {
                Token token = ungetTokens.pop();
                if(token != null) {
                    Log.i("TokenStream", "unget token: '" + token.getStringValue() + "'");
                    s += token.getStringValue();
                }
            }
        }

        if(chars.size() > 0) {
            char ch;

            while(chars.size() > 0 && chars.peekFirst() != null) {
                ch = chars.removeFirst();
                column++;

                Log.i("TokenStream", "char: '" + ch + "'");

                if(ch == '\'') {
                    ungetTokens.push(new Token(Token.Type.QUOTE));
                    return new Token(Token.Type.STRING, s);
                }
                else {
                    s += ch;
                }
            }

            return new Token(Token.Type.STRING, s);
        }
        else {
            return new Token(Token.Type.STRING, "");
        }
    }


    public void unget(Token token) {
        ungetTokens.push(token);
    }

    public boolean hasTokens() {
        if(ungetTokens != null && ungetTokens.size() > 0) {
            return true;
        }
        else if(chars != null && chars.size() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public int getColumn() {
        return column;
    }

    public void unclog() {
        if(ungetTokens.size() > 0) {
            while(ungetTokens.size() > 0) {
                Token token = ungetTokens.pop();
                if(token != null && token.equals(Token.Type.RCURLYBRACE)) {
                    return;
                }
            }
        }

        if(chars.size() > 0) {
            char ch;

            while(chars.size() > 0 && chars.peekFirst() != null) {
                ch = chars.removeFirst();
                column++;

                if(ch == '}') {
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        String s = "";
        for(int i = 0; i < chars.size(); i++) {
            s += chars.get(i);
        }
        return s;
    }
}
