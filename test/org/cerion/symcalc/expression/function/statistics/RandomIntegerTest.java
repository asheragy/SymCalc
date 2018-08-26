package org.cerion.symcalc.expression.function.statistics;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.ListExpr;
import org.cerion.symcalc.expression.function.statistics.RandomInteger;
import org.cerion.symcalc.expression.number.IntegerNum;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RandomIntegerTest {

    @Test
    public void binary() {
        Expr e = new RandomInteger();

        int[] count = { 0, 0 };

        for(int i = 0; i < 10000; i++) {
            IntegerNum n = (IntegerNum) e.eval();
            int x = n.intValue();
            count[x]++;
        }

        assertTrue("0 count = " + count[0], count[0] > 4800);
        assertTrue("1 count = " + count[1], count[1] > 4800);
    }

    @Test
    public void maxValue() {
        Expr e = new RandomInteger(new IntegerNum(10));
        int[] found = { 0, 5, 10 };

        for(int i = 0; i < 10000; i++) {
            IntegerNum n = e.eval().asInteger();
            int rand = n.intValue();
            if(rand == 0)
                found[0] = -1;
            else if(rand == 5)
                found[1] = -1;
            else if (rand == 10)
                found[2] = -1;

            if (found[0] == -1 && found[1] == -1 && found[2] == -1)
                return;
        }

        assertFalse(true);
    }

    @Test
    public void range() {
        Expr e = new RandomInteger(new ListExpr(new IntegerNum(10), new IntegerNum(20)));
        int[] found = { 10, 15, 20 };

        for(int i = 0; i < 10000; i++) {
            IntegerNum n = e.eval().asInteger();
            int rand = n.intValue();
            if(rand == 10)
                found[0] = -1;
            else if(rand == 15)
                found[1] = -1;
            else if (rand == 20)
                found[2] = -1;
            else if (rand < 10)
                assertFalse(true);
            else if (rand > 20)
                assertFalse(true);

            if (found[0] == -1 && found[1] == -1 && found[2] == -1)
                return;
        }

        assertFalse(true);
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