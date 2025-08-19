package com.oizys.study.ast;

import com.oizys.study.Lexical;

/**
 * @author wyn
 * Created on 2025/8/18
 */
public abstract class AstNode {
    public Lexical lex;
    public AstNode left;
    public AstNode right;
    public Object value;

    public abstract void accept(AstVisitor  visitor);
    public abstract AstNode parse();

    public AstNode(Lexical lex) {
       this.lex = lex;
    }
}
