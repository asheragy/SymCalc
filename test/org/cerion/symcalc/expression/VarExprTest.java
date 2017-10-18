package org.cerion.symcalc.expression;

import org.cerion.symcalc.expression.function.arithmetic.Plus;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class VarExprTest {
    @Test
    public void equals() {
        VarExpr v1 = new VarExpr("x");
        VarExpr v2 = new VarExpr("y");
        VarExpr v3 = new VarExpr("x");

        assertNotEquals(v1, v2);
        assertNotEquals(v1, new IntegerNum(5));
        assertEquals(v1, v3);
    }

    @Test
    public void eval() {
        VarExpr x = new VarExpr("x");

        x.getEnv().setVar("x", new IntegerNum(5));
        assertEquals(new IntegerNum(5), x.eval());

        x.getEnv().setVar("x", new IntegerNum(2));
        assertEquals(new IntegerNum(2), x.eval());
    }

    @Test
    public void evalFunction() {
        Expr e = new Plus(new VarExpr("x"), new VarExpr("x"));

        e.getEnv().setVar("x", new IntegerNum(5));
        assertEquals(new IntegerNum(10), e.eval());

        e.getEnv().setVar("x", new IntegerNum(3));
        assertEquals(new IntegerNum(6), e.eval());
    }

}