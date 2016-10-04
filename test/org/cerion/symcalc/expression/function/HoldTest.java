package org.cerion.symcalc.expression.function;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class HoldTest {

    @Test
    public void basicEval() {
        Expr e = new Hold(new Plus(IntegerNum.ONE, IntegerNum.ONE));
        e = e.eval();

        assertTrue(e.isFunction("hold"));
        assertEquals(1, e.size());
        assertTrue(e.get(0).isFunction("plus"));
    }
}