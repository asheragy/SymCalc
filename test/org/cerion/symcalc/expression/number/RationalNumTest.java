package org.cerion.symcalc.expression.number;

import org.cerion.symcalc.expression.Expr;
import org.cerion.symcalc.expression.function.Plus;
import org.cerion.symcalc.expression.function.Subtract;
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

    @Test
    public void eval_reduces() {
        assertEquals(new RationalNum(2,4), new RationalNum(2,4));
        assertEquals(new RationalNum(1,2), new RationalNum(2,4).eval());
    }

    @Test
    public void addition() {
        //Integer
        assertEquals(new IntegerNum(1), new Plus(new RationalNum(1,2), new RationalNum(1,2)).eval());
        assertEquals(new IntegerNum(1), new Plus(new RationalNum(1,2), new RationalNum(2,4)).eval());
        assertEquals(new IntegerNum(0), new Plus(new RationalNum(-1,2), new RationalNum(2,4)).eval());
        assertEquals(IntegerNum.TWO, new Plus(new RationalNum(1,1), IntegerNum.ONE).eval());

        //Rational
        assertEquals(new RationalNum(3,2), new Plus(new RationalNum(1,1), new RationalNum(1,2)).eval());
        assertEquals(new RationalNum(1,2), new Plus(new RationalNum(-1,2), IntegerNum.ONE).eval());
    }

    @Test
    public void subtract() {
        //Integer
        assertEquals(IntegerNum.ZERO, new Subtract(new RationalNum(1,2), new RationalNum(1,2)).eval());
        assertEquals(IntegerNum.ZERO, new Subtract(new RationalNum(1,2), new RationalNum(2,4)).eval());
        assertEquals(IntegerNum.TWO, new Subtract(new RationalNum(5,2), new RationalNum(1,2)).eval());
        assertEquals(new IntegerNum(-5), new Subtract(new RationalNum(1,2), new RationalNum(11,2)).eval());

        //Rational
        assertEquals(new RationalNum(1,2), new Subtract(new RationalNum(1,1), new RationalNum(1,2)).eval());
        assertEquals(new RationalNum(-1,2), new Subtract(new RationalNum(1,2), IntegerNum.ONE).eval());
    }
}