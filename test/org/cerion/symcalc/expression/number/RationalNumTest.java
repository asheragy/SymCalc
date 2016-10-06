package org.cerion.symcalc.expression.number;

import org.junit.Test;

import static org.junit.Assert.*;


public class RationalNumTest {

    @Test
    public void equals() {
        assertEquals(new RationalNum(4,5), new RationalNum(4,5));
        assertNotEquals(new RationalNum(4,5), new RationalNum(4,4));
        assertNotEquals(new RationalNum(4,5), new RationalNum(5,4));
    }

    @Test
    public void negate() {
        assertEquals(new RationalNum(4,5), new RationalNum(-4,5).negate());
        assertEquals(new RationalNum(-4,5), new RationalNum(4,5).negate());
        assertEquals(new RationalNum(-4,5), new RationalNum(-4,-5).negate());
        assertEquals(new RationalNum(4,5), new RationalNum(4,-5).negate());
    }
}