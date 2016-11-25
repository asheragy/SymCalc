package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.function.integer.EvenQ;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class SelectTest {

    @Test
    public void basicTest() {
        ListExpr list = new ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.TWO, new IntegerNum(3));
        ListExpr expected = new ListExpr(IntegerNum.ZERO, IntegerNum.TWO);

        Expr e = new Select(list, new EvenQ());
        assertEquals(expected, e.eval());
    }
}