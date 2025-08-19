package com.oizys.study.ast;

import com.oizys.study.Lexical;
import com.oizys.study.TokenType;
import com.oizys.study.ast.express.BiOperateExpress;
import com.oizys.study.ast.express.BiOperateExpress.BiOperateType;
import com.oizys.study.ast.express.NumExpress;

/**
 * @author wyn
 * Created on 2025/8/18
 */
public class ExpressionNode extends AstNode{

    protected BiOperateType type;

    public ExpressionNode(Lexical lex) {
        super(lex);
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.accept(this);
    }

    @Override
    public ExpressionNode parse() {
        this.left = parseAddExpr();
        return this;
    }

    public ExpressionNode parseAddExpr() {
        ExpressionNode left = parseMultipartExpr();
        while (lex.match(TokenType.ADD)
                || lex.match(TokenType.SUB)) {

            BiOperateExpress node = new BiOperateExpress(lex);
            if (lex.match(TokenType.ADD)) {
                node.type = BiOperateType.ADD;
                node.value = "+";
            } else {
                node.type = BiOperateType.SUB;
                node.value = "-";
            }
            lex.getNextToken();
            node.left = left;
            node.right = parseMultipartExpr();
            left = node;
        }
        return left;
    }

    public ExpressionNode parseMultipartExpr() {
        ExpressionNode left = parsePrimaryExpr();
        while (lex.match(TokenType.MUL)
                || lex.match(TokenType.DIV)) {
            BiOperateExpress node = new BiOperateExpress(lex);
            if (lex.match(TokenType.MUL)) {
                node.type = BiOperateType.MUL;
                node.value = "*";
            }
            else {
                node.type = BiOperateType.DIV;
                node.value = "/";
            }
            lex.getNextToken();
            node.left = left;
            node.right = parsePrimaryExpr();
            left = node;
        }
        return left;
    }

    public ExpressionNode parsePrimaryExpr() {
        if (lex.match(TokenType.EOF)) {
            return null;
        }
        if (lex.match(TokenType.NUMBER)) {
            return parseNumExpr();
        }
        lex.getNextToken();
        return parsePrimaryExpr();
    }

    ExpressionNode parseNumExpr()
    {
        NumExpress node = new NumExpress(lex);
        node.value = ((int)lex.getCurrentToken().getValue());
        lex.getNextToken();
        return node;
    }


}
