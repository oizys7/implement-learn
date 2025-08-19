package com.oizys.study.ast.express;


import com.oizys.study.Lexical;
import com.oizys.study.ast.AstVisitor;
import com.oizys.study.ast.ExpressionNode;

/**
 * 双操作节点
 *
 * @author wyn
 * @date 2025/08/18
 */
public class BiOperateExpress extends ExpressionNode {


    public BiOperateExpress(Lexical lex) {
        super(lex);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.accept(this);
    }

    @Override
    public ExpressionNode parse() {
        return null;
    }


    public enum BiOperateType {
        ADD, SUB, MUL, DIV
    }
}
