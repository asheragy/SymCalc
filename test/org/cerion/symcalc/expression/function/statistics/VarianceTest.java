package org.cerion.symcalc.expression.function.statistics;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class VarianceTest {

    @Test
    public void basicIntegerTest() {
        ListExpr list = new ListExpr(new IntegerNum(600), new IntegerNum(470),
                new IntegerNum(170), new IntegerNum(430), new IntegerNum(300));

        Expr e = new Variance(list).eval();
        assertEquals(new IntegerNum(21704), e);
    }

    @Test
    public void basicTest() {
        ListExpr list = new ListExpr(RealNum.Companion.create(1.1), RealNum.Companion.create(1.2), RealNum.Companion.create(1.3));
        Expr e = new Variance(list).eval();
        assertEquals(RealNum.Companion.create(0.00666).toDouble(), ((RealNum)e).toDouble(), 0.00001);

        list = new ListExpr(RealNum.Companion.create(1.21), RealNum.Companion.create(3.4), RealNum.Companion.create(2.0),
                RealNum.Companion.create(4.66), RealNum.Companion.create(1.5), RealNum.Companion.create(5.61), RealNum.Companion.create(7.22));
        e = new Variance(list).eval();
        assertEquals(RealNum.Companion.create(4.42390).toDouble(), ((RealNum)e).toDouble(), 0.00001);
    }
}