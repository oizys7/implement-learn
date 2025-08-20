package com.oizys.study;


import com.oizys.study.core.Lexical;
import com.oizys.study.core.Parser;
import com.oizys.study.core.Token;
import com.oizys.study.visitor.CodeGen;
import com.oizys.study.visitor.PrintVisitor;
import org.junit.jupiter.api.Test;

/**
 * @author wyn
 * Created on 2025/8/19
 */
class MainTest {
    public static final String CODE = "5+(1-3)*4/2";
    @Test
    void testLex() {
        Lexical lex = new Lexical(CODE);
        for (Token token : lex) {
            System.out.print(token.getValue() + " ");
        }
    }

    @Test
    void testParser() {
        Lexical lex = new Lexical(CODE);
        for (Token token : lex) {
            System.out.print(token.getValue() + " ");
        }

        Parser parser = new Parser(lex);
        parser.parse();
        System.out.println();
        parser.accept(new PrintVisitor());
    }

    void test() {
        Lexical lex = new Lexical(CODE);
        Parser parser = new Parser(lex);
        parser.parse();
        System.out.println();
        parser.accept(new CodeGen());
    }

}