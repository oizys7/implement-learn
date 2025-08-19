package com.oizys.study;

import com.oizys.study.ast.PrintVisitor;
import com.oizys.study.ast.ProgramNode;

/**
 * @author wyn
 * Created on 2025/8/18
 */
public class Main {
    public static final String CODE = "5+1-3*4/2";
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("请输入代码");
        }
        Lexical lex = new Lexical(args[0]);
        Parser parser = new Parser(lex);
        parser.parse();
        System.out.println();
        parser.accept(new CodeGen());
    }

    public static void test() {
        Lexical lex = new Lexical(CODE);
        for (Token token : lex) {
            System.out.print(token.getValue() + " ");
        }
        Parser parser = new Parser(lex);
        parser.parse();
        System.out.println();
        parser.accept(new CodeGen());
    }
}