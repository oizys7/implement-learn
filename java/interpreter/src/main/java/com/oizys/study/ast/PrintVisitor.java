package com.oizys.study.ast;

import com.oizys.study.Lexical;

/**
 * @author wyn
 * Created on 2025/8/19
 */
public class PrintVisitor extends AstNode implements AstVisitor  {

    public PrintVisitor(Lexical lex) {
        super(lex);
    }

    @Override
    public void accept(AstNode node) {
        if (node == null) {
            return;
        }
        accept( node.right);
        accept( node.left);
        if (node.value != null) {
            System.out.print(node.value + " ");
        }
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
