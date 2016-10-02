package org.cerion.symcalc.expression.number;

import org.junit.Test;

import static org.junit.Assert.*;

public class RealNumTest {
    @Test
    public void equals() {

        RealNum n1 = new RealNum(5.0);
        RealNum n2 = new RealNum(6.0);
        RealNum n3 = new RealNum(5.0);
        IntegerNum n4 = new IntegerNum(5);

        assertNotEquals(n1, n2);
        assertNotEquals(n1, n4); //TODO for now this is okay but maybe should be equal
        assertEquals(n1, n3);

        //TODO add BigDecimal cases or switch class to only use that type
    }

}