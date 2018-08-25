package org.cerion.symcalc.expression;

import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListExprTest {

    @Test
    public void equals() {

        ListExpr l1 = new ListExpr(new VarExpr("x"), new IntegerNum(5), BoolExpr.FALSE);
        ListExpr l2 = new ListExpr(new VarExpr("x"), new IntegerNum(6), BoolExpr.FALSE);
        ListExpr l3 = new ListExpr(new VarExpr("x"), new IntegerNum(5), BoolExpr.FALSE);

        assertTrue(l1.equals(l3));
        assertFalse(l1.equals(l2));
    }

    @Test
    public void equals_nested() {
        ListExpr a = new ListExpr(new ListExpr(IntegerNum.ONE), new ListExpr(new VarExpr("a")));
        ListExpr b = new ListExpr(new ListExpr(IntegerNum.ONE), new ListExpr(new VarExpr("a")));
        assertTrue(a.equals(b));

        a = new ListExpr(new ListExpr(new VarExpr("5"), new IntegerNum(2)), new ListExpr(new VarExpr("a"), new IntegerNum(1)), new ListExpr(new VarExpr("2"), new IntegerNum(1)));
        b = new ListExpr(new ListExpr(new VarExpr("5"), new IntegerNum(2)), new ListExpr(new VarExpr("a"), new IntegerNum(1)), new ListExpr(new VarExpr("2"), new IntegerNum(1)));
        assertTrue(a.equals(b));
    }
}