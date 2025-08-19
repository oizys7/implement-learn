package com.oizys.study;

import com.oizys.study.ast.*;
import com.oizys.study.ast.BiOperateNode;

public class CodeGen implements AstVisitor {

    private int stackIndex = 0;

    public void accept(ProgramNode node) {
        StringBuilder sb = new StringBuilder();
        System.out.println(""" 
            \t.text
            \t.globl prog
            prog:
            \tpush %rbp
            \tmov %rsp, %rbp
            \tsub $32, %rsp""");
        node.left.accept(this);

        System.out.println("""
            \tmov %rbp, %rsp
            \tpop %rbp
            \tret""");
    }

    public void accept(BiOperateNode node) {
        node.right.accept(this);
        push();
        node.left.accept(this);
        pop("%rdi");

        switch (node.getType()) {
            case ADD -> System.out.println("\tadd %rdi, %rax");
            case SUB -> System.out.println("\tsub %rdi, %rax");
            case MUL -> System.out.println("\timul %rdi, %rax");
            case DIV -> {
                System.out.println("\tcqo");
                System.out.println("\tidiv %rdi");
            }
            default -> throw new RuntimeException("不支持的操作符");
        }
    }

    public void accept(ConstantNode node) {
        System.out.println("\tmov $"+ node.value +", %rax");
    }

    public void push() {
        System.out.println("\tpush %rax");
        stackIndex++;
    }
    public void pop(String value) {
        System.out.println("\tpop " + value);
        stackIndex--;
    }

}
