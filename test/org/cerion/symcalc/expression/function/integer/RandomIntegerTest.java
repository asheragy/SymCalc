package org.cerion.symcalc.expression.function.integer;

import org.cerion.symcalc.Environment;
import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.FunctionExpr;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomIntegerTest {

    @Test
    public void binary() {
        Expr e = new RandomInteger();

        int[] count = { 0, 0 };

        for(int i = 0; i < 10000; i++) {
            IntegerNum n = (IntegerNum) e.eval();
            int x = n.toInteger();
            count[x]++;
        }

        //if this fails occasionally it might still be okay
        assertTrue("0 count = " + count[0], count[0] > 4900);
        assertTrue("1 count = " + count[1], count[1] > 4900);
    }

    @Test
    public void numbersAreRandom() {
        Expr e = new RandomInteger(new IntegerNum(10));

        IntegerNum prev = new IntegerNum(-99);
        for(int i = 0; i < 5; i++) {
            IntegerNum curr = (IntegerNum)e.eval();
            if(!prev.equals(curr))
                return;

        }

        // All numbers were the same
        assertFalse(true);
    }
}