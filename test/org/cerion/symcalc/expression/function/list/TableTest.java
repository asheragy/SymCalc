package org.cerion.symcalc.expression.function.list;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.function.statistics.RandomInteger;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TableTest {

    @Test
    public void exprDelayedEval() {
        Expr r = new RandomInteger();
        ListExpr params = new ListExpr(new IntegerNum(20));
        Expr e = new Table(r,params);

        e = e.eval();
        assertEquals(20, e.size());
        assertTrue(e.get(0).isInteger());
        IntegerNum sum = IntegerNum.ZERO;
        for(int i = 0; i < e.size(); i++)
            sum = sum.add((IntegerNum)e.get(i));

        //Result should be list of 0s and 1s, verify there is at least a few of each
        assertTrue(sum.toInteger() > 2);
        assertTrue(sum.toInteger() < 18);
    }
}