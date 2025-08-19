package com.oizys.study.ast;

import com.oizys.study.Lexical;

/**
 * @author wyn
 * Created on 2025/8/19
 */
public class ConstantNode extends AstNode{
    public ConstantNode(Lexical lex) {
        super(lex);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.accept(this);
    }

    @Override
    public AstNode parse() {
        return null;
    }
}
