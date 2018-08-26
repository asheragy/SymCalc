package org.cerion.symcalc.expression.function.statistics;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.cerion.symcalc.expression.number.RationalNum;
import org.cerion.symcalc.expression.number.RealNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class MeanTest {

    @Test
    public void basicTest() {
        ListExpr list = new ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO, new IntegerNum(3));
        Expr e = new Mean(list).eval();
        assertEquals(new RationalNum(new IntegerNum(3), IntegerNum.TWO), e);

        list = new ListExpr(new IntegerNum(6), new RationalNum(87,3), RealNum.create(23.542));
        e = new Mean(list).eval();
        assertEquals(RealNum.create(19.514), e);
    }
}