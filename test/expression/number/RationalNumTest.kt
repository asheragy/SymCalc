package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.core.Hold
import org.cerion.symcalc.expression.function.core.N
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals


class RationalNumTest : NumberTestBase() {

    @Test
    fun equals() {
        assertEquals(RationalNum(4, 5), RationalNum(4, 5))
        assertNotEquals(RationalNum(4, 5), RationalNum(4, 4))
        assertNotEquals(RationalNum(4, 5), RationalNum(5, 4))
    }

    @Test
    fun negate() {
        assertEquals(RationalNum(4, 5), RationalNum(-4, 5).unaryMinus())
        assertEquals(RationalNum(-4, 5), RationalNum(4, 5).unaryMinus())
        assertEquals(RationalNum(-4, 5), RationalNum(-4, -5).unaryMinus())
        assertEquals(RationalNum(4, 5), RationalNum(4, -5).unaryMinus())
    }

    @Test
    fun eval_reduces() {
        assertEquals(IntegerNum.TWO, RationalNum(2, 4).numerator)
        assertEquals(IntegerNum.ONE, (RationalNum(2, 4).eval() as RationalNum).numerator)

        // Except with Hold
        assertEquals(IntegerNum.TWO, Hold(RationalNum(2, 4)).eval()[0, false].asNumber().asRational().numerator)
    }

    @Test
    fun eval_toReal() {
        assertEquals(RealNum.create(0.3333333333333333), N(RationalNum(1,3)).eval())

        val bigDec = N(RationalNum(1,3), IntegerNum(50)).eval().asReal()
        assertEquals(50, bigDec.precision)
        assertEquals(RealNum.create("0.33333333333333333333333333333333333333333333333333"), bigDec)
    }

    @Test
    fun addition() {
        //Integer
        assertAdd(IntegerNum(1), RationalNum(1, 2), RationalNum(1, 2))
        assertEquals(IntegerNum(1), Plus(RationalNum(1, 2), RationalNum(2, 4)).eval())
        assertEquals(IntegerNum(0), Plus(RationalNum(-1, 2), RationalNum(2, 4)).eval())
        assertEquals(IntegerNum.TWO, Plus(RationalNum(1, 1), IntegerNum.ONE).eval())

        //Rational
        assertEquals(RationalNum(3, 2), Plus(RationalNum(1, 1), RationalNum(1, 2)).eval())
        assertEquals(RationalNum(1, 2), Plus(RationalNum(-1, 2), IntegerNum.ONE).eval())
    }

    @Test
    fun subtract() {
        //Integer
        assertEquals(IntegerNum.ZERO, Subtract(RationalNum(1, 2), RationalNum(1, 2)).eval())
        assertEquals(IntegerNum.ZERO, Subtract(RationalNum(1, 2), RationalNum(2, 4)).eval())
        assertEquals(IntegerNum.TWO, Subtract(RationalNum(5, 2), RationalNum(1, 2)).eval())
        assertEquals(IntegerNum(-5), Subtract(RationalNum(1, 2), RationalNum(11, 2)).eval())

        //Rational
        assertEquals(RationalNum(1, 2), Subtract(RationalNum(1, 1), RationalNum(1, 2)).eval())
        assertEquals(RationalNum(-1, 2), Subtract(RationalNum(1, 2), IntegerNum.ONE).eval())
    }
}