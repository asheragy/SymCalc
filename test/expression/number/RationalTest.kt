package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.core.Hold
import org.cerion.symcalc.expression.function.core.N
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import kotlin.test.assertFailsWith


class RationalTest : NumberTestBase() {

    @Test
    fun equals() {
        assertEquals(Rational(4, 5), Rational(4, 5))
        assertNotEquals(Rational(4, 5), Rational(4, 4))
        assertNotEquals(Rational(4, 5), Rational(5, 4))
    }

    @Test
    fun compareTo() {
        assertEquals(-1, Rational(1,3).compareTo(IntegerNum(1)))
        assertEquals(1, Rational(1,2).compareTo(IntegerNum(0)))
        assertEquals(0, Rational(16,4).compareTo(IntegerNum(4)))

        assertEquals(-1, Rational(1,3).compareTo(Rational(1,2)))
        assertEquals(1, Rational(10,3).compareTo(Rational(7,3)))
        assertEquals(0, Rational(1,2).compareTo(Rational(2,4)))

        assertEquals(-1, Rational(-1,2).compareTo(RealNum_Double(-0.2)))
        assertEquals(1, Rational(1,2).compareTo(RealNum_Double(0.4)))
        assertEquals(0, Rational(1,2).compareTo(RealNum_Double(0.5)))

        assertEquals(-1, Rational(1,3).compareTo(Complex(1,0)))
        assertEquals(1, Rational(4,3).compareTo(Complex(1,0)))
    }

    @Test
    fun compareTo_complex() {
        assertFailsWith<UnsupportedOperationException> { Rational(1,2).compareTo(Complex(IntegerNumTest.zero, IntegerNumTest.one)) }
    }

    @Test
    fun negate() {
        assertEquals(Rational(4, 5), Rational(-4, 5).unaryMinus())
        assertEquals(Rational(-4, 5), Rational(4, 5).unaryMinus())
        assertEquals(Rational(-4, 5), Rational(-4, -5).unaryMinus())
        assertEquals(Rational(4, 5), Rational(4, -5).unaryMinus())
    }

    @Test
    fun eval_normalizesNegative() {
        var n = Rational(2,-3)
        assertEquals(IntegerNum(2), n.numerator)
        assertEquals(IntegerNum(-3), n.denominator)

        n = n.eval() as Rational
        assertEquals(IntegerNum(-2), n.numerator)
        assertEquals(IntegerNum(3), n.denominator)

        n = Rational(-2,-3).eval() as Rational
        assertEquals(IntegerNum(2), n.numerator)
        assertEquals(IntegerNum(3), n.denominator)
    }

    @Test
    fun eval_reduces() {
        assertEquals(IntegerNum.TWO, Rational(2, 4).numerator)
        assertEquals(IntegerNum.ONE, (Rational(2, 4).eval() as Rational).numerator)

        // Except with Hold
        assertEquals(IntegerNum.TWO, Hold(Rational(2, 4)).eval()[0].asNumber().asRational().numerator)
    }

    @Test
    fun eval_precision() {
        assertEquals(RealNum_BigDecimal("0"), Rational(0,1).eval(10))
    }

    @Test
    fun eval_toReal() {
        assertEquals(RealNum_Double(0.3333333333333333), N(Rational(1,3)).eval())

        val bigDec = N(Rational(1,3), IntegerNum(50)).eval().asNumber().asBigDec()
        assertEquals(50, bigDec.precision)
        assertEquals(RealNum_BigDecimal("0.33333333333333333333333333333333333333333333333333"), bigDec)
    }

    @Test
    fun addition() {
        //Integer
        assertAdd(IntegerNum(1), Rational(1, 2), Rational(1, 2))
        assertEquals(IntegerNum(1), Plus(Rational(1, 2), Rational(2, 4)).eval())
        assertEquals(IntegerNum(0), Plus(Rational(-1, 2), Rational(2, 4)).eval())
        assertEquals(IntegerNum.TWO, Plus(Rational(1, 1), IntegerNum.ONE).eval())

        //Rational
        assertEquals(Rational(3, 2), Plus(Rational(1, 1), Rational(1, 2)).eval())
        assertEquals(Rational(1, 2), Plus(Rational(-1, 2), IntegerNum.ONE).eval())
    }

    @Test
    fun subtract() {
        //Integer
        assertEquals(IntegerNum.ZERO, Subtract(Rational(1, 2), Rational(1, 2)).eval())
        assertEquals(IntegerNum.ZERO, Subtract(Rational(1, 2), Rational(2, 4)).eval())
        assertEquals(IntegerNum.TWO, Subtract(Rational(5, 2), Rational(1, 2)).eval())
        assertEquals(IntegerNum(-5), Subtract(Rational(1, 2), Rational(11, 2)).eval())

        //Rational
        assertEquals(Rational(1, 2), Subtract(Rational(1, 1), Rational(1, 2)).eval())
        assertEquals(Rational(-1, 2), Subtract(Rational(1, 2), IntegerNum.ONE).eval())
    }
}