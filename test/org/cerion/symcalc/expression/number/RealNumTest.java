package org.cerion.symcalc.expression.number;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RealNumTest {
    @Test
    public void equals() {

        RealNum n1 = RealNum.Companion.create(5.0);
        RealNum n2 = RealNum.Companion.create(6.0);
        RealNum n3 = RealNum.Companion.create(5.0);
        IntegerNum n4 = new IntegerNum(5);

        assertNotEquals(n1, n2);
        assertNotEquals(n1, n4); //For now this is okay but maybe should be equal
        assertEquals(n1, n3);

        //Add BigDecimal cases or switch class to only use that type
    }

    @Test
    public void negate() {
        assertEquals(0, RealNum.Companion.create(0.0).unaryMinus().toDouble(), 0.01);
        assertEquals(-1, RealNum.Companion.create(1.0).unaryMinus().toDouble(), 0.01);
        assertEquals(1, RealNum.Companion.create(-1.0).unaryMinus().toDouble(), 0.01);
    }
}