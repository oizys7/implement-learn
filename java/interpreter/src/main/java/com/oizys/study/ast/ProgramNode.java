package com.oizys.study.ast;

import com.oizys.study.core.Lexical;
import com.oizys.study.visitor.AstVisitor;

/**
 * @author wyn
 * Created on 2025/8/18
 */
public class ProgramNode extends AstNode{
    public ProgramNode(Lexical lex) {
        super(lex);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.accept(this);
    }

    @Override
    public ProgramNode parse() {
        this.left = new BiOperateNode(lex).parse();
        return this;
    }
}
