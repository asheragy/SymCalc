package org.cerion.symcalc.expression.number;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class ComplexNumTest {

    @Test
    public void equals() {
        assertEquals(new ComplexNum(0,0), ComplexNum.ZERO);
        assertEquals(new ComplexNum(1,0).eval(), IntegerNum.ONE);
        assertNotEquals(new ComplexNum(1,1), IntegerNum.ONE);
        assertEquals(new ComplexNum(5,6), new ComplexNum(5,6));
        assertNotEquals(new ComplexNum(5,6), new ComplexNum(5,7));
        assertNotEquals(new ComplexNum(5,6), new ComplexNum(6,6));
    }

    @Test
    public void negate() {
        assertEquals(new ComplexNum(0,0), new ComplexNum(0,0).negate());
        assertEquals(new ComplexNum(-1,0), new ComplexNum(1,0).negate());
        assertEquals(new ComplexNum(0,-1), new ComplexNum(0,1).negate());
        assertEquals(new ComplexNum(-1,-1), new ComplexNum(1,1).negate());
        assertEquals(new ComplexNum(1,0), new ComplexNum(-1,0).negate());
        assertEquals(new ComplexNum(0,1), new ComplexNum(0,-1).negate());
        assertEquals(new ComplexNum(1,1), new ComplexNum(-1,-1).negate());
        assertEquals(new ComplexNum(-1,1), new ComplexNum(1,-1).negate());
        assertEquals(new ComplexNum(1,-1), new ComplexNum(-1,1).negate());
    }

}