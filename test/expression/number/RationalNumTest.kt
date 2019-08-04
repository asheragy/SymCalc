package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.core.Hold
import org.cerion.symcalc.expression.function.core.N
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import kotlin.test.assertFailsWith


class RationalNumTest : NumberTestBase() {

    @Test
    fun equals() {
        assertEquals(RationalNum(4, 5), RationalNum(4, 5))
        assertNotEquals(RationalNum(4, 5), RationalNum(4, 4))
        assertNotEquals(RationalNum(4, 5), RationalNum(5, 4))
    }

    @Test
    fun compareTo() {
        assertEquals(-1, RationalNum(1,3).compareTo(IntegerNum(1)))
        assertEquals(1, RationalNum(1,2).compareTo(IntegerNum(0)))
        assertEquals(0, RationalNum(16,4).compareTo(IntegerNum(4)))

        assertEquals(-1, RationalNum(1,3).compareTo(RationalNum(1,2)))
        assertEquals(1, RationalNum(10,3).compareTo(RationalNum(7,3)))
        assertEquals(0, RationalNum(1,2).compareTo(RationalNum(2,4)))

        assertEquals(-1, RationalNum(-1,2).compareTo(RealNum.create(-0.2)))
        assertEquals(1, RationalNum(1,2).compareTo(RealNum.create(0.4)))
        assertEquals(0, RationalNum(1,2).compareTo(RealNum.create(0.5)))

        assertEquals(-1, RationalNum(1,3).compareTo(ComplexNum(1,0)))
        assertEquals(1, RationalNum(4,3).compareTo(ComplexNum(1,0)))
    }

    @Test
    fun compareTo_complex() {
        assertFailsWith<UnsupportedOperationException> { RationalNum(1,2).compareTo(ComplexNum(IntegerNumTest.zero, IntegerNumTest.one)) }
    }

    @Test
    fun negate() {
        assertEquals(RationalNum(4, 5), RationalNum(-4, 5).unaryMinus())
        assertEquals(RationalNum(-4, 5), RationalNum(4, 5).unaryMinus())
        assertEquals(RationalNum(-4, 5), RationalNum(-4, -5).unaryMinus())
        assertEquals(RationalNum(4, 5), RationalNum(4, -5).unaryMinus())
    }

    @Test
    fun eval_normalizesNegative() {
        var n = RationalNum(2,-3)
        assertEquals(IntegerNum(2), n.numerator)
        assertEquals(IntegerNum(-3), n.denominator)

        n = n.eval() as RationalNum
        assertEquals(IntegerNum(-2), n.numerator)
        assertEquals(IntegerNum(3), n.denominator)

        n = RationalNum(-2,-3).eval() as RationalNum
        assertEquals(IntegerNum(2), n.numerator)
        assertEquals(IntegerNum(3), n.denominator)
    }

    @Test
    fun eval_reduces() {
        assertEquals(IntegerNum.TWO, RationalNum(2, 4).numerator)
        assertEquals(IntegerNum.ONE, (RationalNum(2, 4).eval() as RationalNum).numerator)

        // Except with Hold
        assertEquals(IntegerNum.TWO, Hold(RationalNum(2, 4)).eval()[0].asNumber().asRational().numerator)
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