package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class JoinTest {

    private static final ListExpr L1 = new ListExpr(IntegerNum.ZERO, IntegerNum.ONE);
    private static final ListExpr L2 = new ListExpr(IntegerNum.TWO);

    @Test
    public void validate() {
        Expr e1 = new ListExpr();
        Expr e2 = IntegerNum.TWO;
        Expr e3 = new ListExpr(IntegerNum.ZERO, IntegerNum.ONE);

        assertTrue( new Join(e1, e2).eval().isError());
        assertTrue( new Join(e2).eval().isError());
        assertTrue( new Join(e2, e3).eval().isError());
    }

    @Test
    public void emptyList() {
        ListExpr e = new ListExpr();
        assertEquals(e, new Join(e).eval());
        assertEquals(e, new Join(e,e).eval());
    }

    @Test
    public void basicTest() {
        Expr e = new Join(L1, L2).eval();

        ListExpr expected = new ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO);
        assertEquals(expected, e);
    }
}