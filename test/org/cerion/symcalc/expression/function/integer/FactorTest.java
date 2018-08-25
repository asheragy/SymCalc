package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;


public class FactorTest {

    @Test
    public void basic() {
        // 2*2
        assertEquals(new ListExpr(IntegerNum.TWO, IntegerNum.TWO), new Factor(new IntegerNum(4)).eval());

        // 2*2*2
        assertEquals(new ListExpr(IntegerNum.TWO, IntegerNum.TWO, IntegerNum.TWO), new Factor(new IntegerNum(8)).eval());

        // 2*2*3
        assertEquals(new ListExpr(IntegerNum.TWO, IntegerNum.TWO, new IntegerNum(3)), new Factor(new IntegerNum(12)).eval());

        // 2*2*3*3
        assertEquals(new ListExpr(IntegerNum.TWO, IntegerNum.TWO, new IntegerNum(3), new IntegerNum(3)), new Factor(new IntegerNum(36)).eval());

        // 5*5
        assertEquals(new ListExpr(new IntegerNum(5), new IntegerNum(5)), new Factor(new IntegerNum(25)).eval());

        // 5*5*7
        assertEquals(new ListExpr(new IntegerNum(5), new IntegerNum(5), new IntegerNum(7)), new Factor(new IntegerNum(175)).eval());

        // 5*5*7*7
        assertEquals(new ListExpr(new IntegerNum(5), new IntegerNum(5), new IntegerNum(7), new IntegerNum(7)), new Factor(new IntegerNum(1225)).eval());
    }

    @Test
    public void largerNumbers() {
        // 137 * 727 * 3803
        assertEquals(new ListExpr(new IntegerNum(137), new IntegerNum(727), new IntegerNum(3803)), new Factor(new IntegerNum(378774997)).eval());
    }
}