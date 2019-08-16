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
        assertEquals(-1, Rational(1,3).compareTo(Integer(1)))
        assertEquals(1, Rational(1,2).compareTo(Integer(0)))
        assertEquals(0, Rational(16,4).compareTo(Integer(4)))

        assertEquals(-1, Rational(1,3).compareTo(Rational(1,2)))
        assertEquals(1, Rational(10,3).compareTo(Rational(7,3)))
        assertEquals(0, Rational(1,2).compareTo(Rational(2,4)))

        assertEquals(-1, Rational(-1,2).compareTo(RealDouble(-0.2)))
        assertEquals(1, Rational(1,2).compareTo(RealDouble(0.4)))
        assertEquals(0, Rational(1,2).compareTo(RealDouble(0.5)))

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
        assertEquals(Integer(2), n.numerator)
        assertEquals(Integer(-3), n.denominator)

        n = n.eval() as Rational
        assertEquals(Integer(-2), n.numerator)
        assertEquals(Integer(3), n.denominator)

        n = Rational(-2,-3).eval() as Rational
        assertEquals(Integer(2), n.numerator)
        assertEquals(Integer(3), n.denominator)
    }

    @Test
    fun eval_reduces() {
        assertEquals(Integer.TWO, Rational(2, 4).numerator)
        assertEquals(Integer.ONE, (Rational(2, 4).eval() as Rational).numerator)

        // Except with Hold
        assertEquals(Integer.TWO, Hold(Rational(2, 4)).eval()[0].asNumber().asRational().numerator)
    }

    @Test
    fun eval_precision() {
        assertEquals(RealBigDec("0"), Rational(0,1).eval(10))
    }

    @Test
    fun eval_toReal() {
        assertEquals(RealDouble(0.3333333333333333), N(Rational(1,3)).eval())

        val bigDec = N(Rational(1,3), Integer(50)).eval().asNumber().asBigDec()
        assertEquals(50, bigDec.precision)
        assertEquals(RealBigDec("0.33333333333333333333333333333333333333333333333333"), bigDec)
    }

    @Test
    fun addition() {
        //Integer
        assertAdd(Integer(1), Rational(1, 2), Rational(1, 2))
        assertEquals(Integer(1), Plus(Rational(1, 2), Rational(2, 4)).eval())
        assertEquals(Integer(0), Plus(Rational(-1, 2), Rational(2, 4)).eval())
        assertEquals(Integer.TWO, Plus(Rational(1, 1), Integer.ONE).eval())

        //Rational
        assertEquals(Rational(3, 2), Plus(Rational(1, 1), Rational(1, 2)).eval())
        assertEquals(Rational(1, 2), Plus(Rational(-1, 2), Integer.ONE).eval())
    }

    @Test
    fun subtract() {
        //Integer
        assertEquals(Integer.ZERO, Subtract(Rational(1, 2), Rational(1, 2)).eval())
        assertEquals(Integer.ZERO, Subtract(Rational(1, 2), Rational(2, 4)).eval())
        assertEquals(Integer.TWO, Subtract(Rational(5, 2), Rational(1, 2)).eval())
        assertEquals(Integer(-5), Subtract(Rational(1, 2), Rational(11, 2)).eval())

        //Rational
        assertEquals(Rational(1, 2), Subtract(Rational(1, 1), Rational(1, 2)).eval())
        assertEquals(Rational(-1, 2), Subtract(Rational(1, 2), Integer.ONE).eval())
    }
}