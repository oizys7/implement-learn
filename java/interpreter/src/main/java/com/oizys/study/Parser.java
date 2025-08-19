package com.oizys.study;

import com.oizys.study.ast.AstNode;
import com.oizys.study.ast.AstVisitor;
import com.oizys.study.ast.ProgramNode;

/**
 * @author wyn
 * Created on 2025/8/18
 */
public class Parser {
    private Lexical lex;

    private AstNode astNode;


    public Parser(Lexical lex) {
        this.lex = lex;
    }
    public void parse() {
        astNode = new ProgramNode(lex);
        astNode.parse();
    }
    public void accept(AstVisitor  visitor) {
        astNode.accept(visitor);
    }
}
