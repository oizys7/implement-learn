package com.oizys.study.ast;

import com.oizys.study.core.Lexical;
import com.oizys.study.enums.BiOperateType;
import com.oizys.study.enums.TokenType;
import com.oizys.study.visitor.AstVisitor;

/**
 * @author wyn
 * Created on 2025/8/18
 */
public class BiOperateNode extends AstNode {

    protected BiOperateType type;

    public BiOperateNode(Lexical lex) {
        super(lex);
    }

    public BiOperateType getType() {
        return type;
    }

    @Override
    public void accept(AstVisitor visitor) {
        visitor.accept(this);
    }

    @Override
    public AstNode parse() {
        return parseAddExpr();
    }

    public AstNode parseAddExpr() {
        AstNode left = parseMultipartExpr();
        while (lex.match(TokenType.ADD)
                || lex.match(TokenType.SUB)) {

            BiOperateNode node = new BiOperateNode(lex);
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

    public AstNode parseMultipartExpr() {
        AstNode left = parsePrimaryExpr();
        while (lex.match(TokenType.MUL)
                || lex.match(TokenType.DIV)) {
            BiOperateNode node = new BiOperateNode(lex);
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

    public AstNode parsePrimaryExpr() {
        if (lex.match(TokenType.EOF)) {
            return null;
        }
        if (lex.match(TokenType.NUMBER)) {
            return parseNumExpr();
        }
        if (lex.match(TokenType.LPAREN) || lex.match(TokenType.RPAREN)) {
            lex.getNextToken();
        }
        lex.getNextToken();
        return parsePrimaryExpr();
    }

    public AstNode parseNumExpr()
    {
        ConstantNode node = new ConstantNode(lex);
        node.value = ((int)lex.getCurrentToken().getValue());
        lex.getNextToken();
        return node;
    }


}
