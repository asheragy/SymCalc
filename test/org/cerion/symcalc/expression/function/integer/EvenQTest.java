package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.NumberExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class EvenQTest {
    @Test
    public void invalidParameterCount() {
        Expr e = new EvenQ(IntegerNum.ONE, IntegerNum.TWO);
        assertTrue(e.eval().isError());

        e = new EvenQ();
        assertTrue(e.eval().isError());
    }

    @Test
    public void nonIntegerInput() {
        ListExpr e = new ListExpr();
        RealNum num = RealNum.Companion.create(3.0);

        assertEquals(BoolExpr.FALSE, new EvenQ(e).eval());
        assertEquals(BoolExpr.FALSE, new EvenQ(num).eval());
    }

    @Test
    public void basicTest() {
        assertEquals(BoolExpr.TRUE, new EvenQ(IntegerNum.ZERO).eval());
        assertEquals(BoolExpr.FALSE, new EvenQ(IntegerNum.ONE).eval());
        assertEquals(BoolExpr.TRUE, new EvenQ(IntegerNum.TWO).eval());

        // negative
        assertEquals(BoolExpr.TRUE, new EvenQ(NumberExpr.parse("-34")).eval());
        assertEquals(BoolExpr.FALSE, new EvenQ(NumberExpr.parse("-35")).eval());

        // Large
        assertEquals(BoolExpr.FALSE, new EvenQ(NumberExpr.parse("1234567890987654321")).eval());
        assertEquals(BoolExpr.FALSE, new EvenQ(NumberExpr.parse("-1234567890987654321")).eval());
        assertEquals(BoolExpr.TRUE, new EvenQ(NumberExpr.parse("1234567890987654322")).eval());
        assertEquals(BoolExpr.TRUE, new EvenQ(NumberExpr.parse("-1234567890987654322")).eval());
    }
}