package com.oizys.study.ast;

/**
 * @author wyn
 * Created on 2025/8/18
 */
public interface AstVisitor {
    void accept(ProgramNode node);

    void accept(BiOperateNode node);

    void accept(ConstantNode node);

}
