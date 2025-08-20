package com.oizys.study.visitor;

import com.oizys.study.ast.AstNode;
import com.oizys.study.ast.BiOperateNode;
import com.oizys.study.ast.ConstantNode;
import com.oizys.study.ast.ProgramNode;

/**
 * @author wyn
 * Created on 2025/8/19
 */
public class PrintVisitor implements AstVisitor  {

    public PrintVisitor() {
    }

    public void acceptAstNode(AstNode node) {
        if (node == null) {
            return;
        }
        acceptAstNode( node.right);
        acceptAstNode( node.left);
        if (node.value != null) {
            System.out.print(node.value + " ");
        }
    }

    @Override
    public void accept(ProgramNode node) {
        acceptAstNode(node);
    }

    @Override
    public void accept(BiOperateNode node) {
        acceptAstNode(node);
    }

    @Override
    public void accept(ConstantNode node) {
        acceptAstNode(node);
    }
}
