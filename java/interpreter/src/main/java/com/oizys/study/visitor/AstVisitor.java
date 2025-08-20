package com.oizys.study.visitor;

import com.oizys.study.ast.BiOperateNode;
import com.oizys.study.ast.ConstantNode;
import com.oizys.study.ast.ProgramNode;

/**
 * @author wyn
 * Created on 2025/8/18
 */
public interface AstVisitor {
    void accept(ProgramNode node);

    void accept(BiOperateNode node);

    void accept(ConstantNode node);

}
