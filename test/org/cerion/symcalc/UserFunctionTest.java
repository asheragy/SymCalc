package org.cerion.symcalc;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.VarExpr;
import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserFunctionTest {

    @Test
    public void basic_substitution() {
        UserFunction uf = new UserFunction("foo", new VarExpr("x"), new VarExpr("x"));

        // Same input
        Expr input = new VarExpr("x");
        Expr output = uf.eval(input);
        assertEquals(input, output);

        // Number
        input = new IntegerNum(5);
        output = uf.eval(input);
        assertEquals(input, output);

        // Another variable
        input = new VarExpr("y");
        output = uf.eval(input);
        assertEquals(input, output);
    }

    @Test
    public void addition() {
        Expr e = new Plus(new VarExpr("x"), new VarExpr("x"));
        UserFunction uf = new UserFunction("foo", e, new VarExpr("x"));

        Expr input = new IntegerNum(5);
        Expr output = uf.eval(input);
        assertEquals(new IntegerNum(10), output);

    }
}