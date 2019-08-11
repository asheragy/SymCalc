package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        // Test commit 3/8/17
        //MathExpr expr = new MathExpr("d");
        //MathExpr expr = new MathExpr("FFT({-1, -1, -1, -1, 1, 1, 1, 1})");
        //MathExpr expr = new MathExpr("RandomInteger(10)");
        //Expr expr = Expr.parse("Greater(1,3)");
        //MathExpr expr = new MathExpr("N(Sin(1))");
        //Expr expr = Expr.parse("1 + 2 + N(1) + Pi + 1 + 2");
        val expr = Expr.parse("1 + 2 + 3")

        expr.print()

        //expr.eval();
        val a = expr.eval()
        //Expr.ExprType t = a.getType();

        println("---------")
        println("Answer = $a")
        a.print()
    }
}
