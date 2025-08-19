package com.oizys.study;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wyn
 * Created on 2025/8/18
 */
public class Lexical implements Iterable< Token> {
    /**
     * 源代码
     */
    private final String sourceCode;

    private Token currentToken;
    private final List<Token> tokenList;

    private int cursor = 0;

    public Lexical(String sourceCode) {
        this.sourceCode = sourceCode;
        this.tokenList = new ArrayList<>(sourceCode.length());
        parseToken(sourceCode);
    }

    public void parseToken(String sourceCode) {
        int index = 0;
        Token currentToken = null;
        while (index < sourceCode.length()) {
            char ch = sourceCode.charAt(index);
            if (ch == ' ') {
                index++;
                continue;
            }
            currentToken = switch (ch) {
                case '+' -> new Token(TokenType.ADD, "+");
                case '-' -> new Token(TokenType.SUB, "-");
                case '*' -> new Token(TokenType.MUL, "*");
                case '/' -> new Token(TokenType.DIV, "/");
                case '(' -> new Token(TokenType.LPAREN, "(");
                case ')' -> new Token(TokenType.RPAREN, ")");
                case '\0' -> new Token(TokenType.EOF, "eof");
                default -> {
                    if (Character.isDigit(ch)) {
                        StringBuilder number = new StringBuilder();
                        while (index < sourceCode.length() && Character.isDigit(sourceCode.charAt(index))) {
                            number.append(sourceCode.charAt(index));
                            index++;
                        }
                        index--;
                        yield new Token(TokenType.NUMBER, Integer.parseInt(number.toString()));
                    }
                    throw new RuntimeException("不支持的字符" + ch);
                }
            };
            index++;
            tokenList.add(currentToken);
        }
    }

    public Boolean match(TokenType type) {
        currentToken = getCurrentToken();
        return null!= currentToken && currentToken.getType().equals(type);
    }
    public Token getCurrentToken() {
        if (null == currentToken) {
            currentToken = tokenList.getFirst();
        }
        return currentToken;
    }

    public void getNextToken() {
        ++cursor;
        if (cursor >= tokenList.size()) {
            currentToken = new Token(TokenType.EOF, null);
        } else {
            currentToken = tokenList.get(cursor);
        }
    }

    @Override
    public Iterator<Token> iterator() {
        return new TokenItr();
    }

    class TokenItr implements Iterator<Token> {
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < tokenList.size();
        }

        @Override
        public Token next() {
            return tokenList.get(index++);
        }
    }
}
