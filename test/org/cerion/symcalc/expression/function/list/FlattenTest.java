package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class FlattenTest {

    private static final ListExpr L1 = new ListExpr(IntegerNum.ONE, IntegerNum.TWO);
    private static final ListExpr L2 = new ListExpr(new IntegerNum(3), new IntegerNum(4));
    private static final ListExpr L3 = new ListExpr(new IntegerNum(5), new IntegerNum(6));

    @Test
    public void validateParameters() {
        Expr e = new Flatten(IntegerNum.ZERO);
        e = e.eval();
        assertTrue(e.isError());
    }

    @Test
    public void singleList() {
        Expr e = new Flatten(new ListExpr(IntegerNum.ZERO, L1)).eval();
        verify(new ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO), e);

        e = new Flatten(new ListExpr(L1, IntegerNum.ZERO)).eval();
        verify(new ListExpr(IntegerNum.ONE, IntegerNum.TWO, IntegerNum.ZERO), e);
    }

    @Test
    public void nestedList() {
        ListExpr list = new ListExpr(IntegerNum.ZERO, L1, new ListExpr(L2,L3));
        ListExpr expected = new ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO,
                new IntegerNum(3), new IntegerNum(4), new IntegerNum(5), new IntegerNum(6));

        verify(expected, new Flatten(list).eval());
    }

    private void verify(ListExpr expected, Expr actual) {
        assertTrue(actual.isList());
        ListExpr l = (ListExpr)actual;

        assertEquals(expected.size(), l.size());
        assertEquals(expected, actual);
    }
}