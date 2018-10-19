package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.BoolExpr;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.NumberExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class OddQTest {

    @Test
    public void invalidParameterCount() {
        Expr e = new OddQ(IntegerNum.ONE, IntegerNum.TWO);
        assertTrue(e.eval().isError());

        e = new OddQ();
        assertTrue(e.eval().isError());
    }

    @Test
    public void nonIntegerInput() {
        ListExpr e = new ListExpr();
        RealNum num = RealNum.Companion.create(3.0);

        assertEquals(BoolExpr.FALSE, new OddQ(e).eval());
        assertEquals(BoolExpr.FALSE, new OddQ(num).eval());
    }

    @Test
    public void basicTest() {
        assertEquals(BoolExpr.FALSE, new OddQ(IntegerNum.ZERO).eval());
        assertEquals(BoolExpr.TRUE, new OddQ(IntegerNum.ONE).eval());
        assertEquals(BoolExpr.FALSE, new OddQ(IntegerNum.TWO).eval());

        // negative
        assertEquals(BoolExpr.FALSE, new OddQ(NumberExpr.parse("-34")).eval());
        assertEquals(BoolExpr.TRUE, new OddQ(NumberExpr.parse("-35")).eval());

        // Large
        assertEquals(BoolExpr.TRUE, new OddQ(NumberExpr.parse("1234567890987654321")).eval());
        assertEquals(BoolExpr.TRUE, new OddQ(NumberExpr.parse("-1234567890987654321")).eval());
        assertEquals(BoolExpr.FALSE, new OddQ(NumberExpr.parse("1234567890987654322")).eval());
        assertEquals(BoolExpr.FALSE, new OddQ(NumberExpr.parse("-1234567890987654322")).eval());
    }
}