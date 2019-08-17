package org.cerion.symcalc.expression.number

import org.cerion.symcalc.exception.OperationException
import org.junit.jupiter.api.Test
import java.math.BigInteger
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IntegerTest {

    private val i0 = Integer(0)
    private val i1 = Integer(1)
    private val i3 = Integer(3)
    private val i5 = Integer(5)
    private val neg = Integer(-17)
    private val big = Integer("99999999999999999999999999999999999999999999")

    @Test
    fun stringConstructor() {
        verify(Integer("0"), 0)
        verify(Integer("5"), 5)
        verify(Integer("-5"), -5)
        verify(Integer("1234567890"), 1234567890)
        verify(Integer("-1234567890"), -1234567890)

        verify(Integer("9999999999999999999999999999999999999999"), BigInteger("9999999999999999999999999999999999999999"))
    }

    @Test
    fun constants() {
        verify(Integer.ZERO, 0)
        verify(Integer.ONE, 1)
        verify(Integer.TWO, 2)
    }

    @Test
    fun negate() {
        verify(-zero, 0)
        verify(-one, -1)
        verify(-two, -2)
        verify(-Integer(-5), 5)
    }

    @Test
    fun addition() {
        //Identity
        verify(i0 + i1, 1)
        verify(i1 + i0, 1)

        //Basic
        verify(i1 + i1, 2)
        verify(i0 + i0, 0)
        verify(i3 + i5, 8)

        //Negative
        verify(neg + i5, -12)
        verify(i5 + neg, -12)
        verify(neg + neg, -34)

        //Big
        verify(big + big, BigInteger("199999999999999999999999999999999999999999998"))
        verify(big + i1, BigInteger("100000000000000000000000000000000000000000000"))
        verify(big + big.unaryMinus(), 0)
    }

    @Test
    fun subtraction() {
        //Zero
        verify(i0 - i0, 0)
        verify(i0 - i1, -1)
        verify(i1 - i0, 1)

        //Basic
        verify(i1 - i1, 0)
        verify(i3 - i5, -2)
        verify(i5 - i3, 2)

        //Negative
        verify(neg - i5, -22)
        verify(i5 - neg, 22)
        verify(neg - neg, 0)

        //Big
        verify(big - big, BigInteger("0"))
        verify(big - i1, BigInteger("99999999999999999999999999999999999999999998"))
        verify(i1 - big, BigInteger("-99999999999999999999999999999999999999999998"))
        verify(big - big.unaryMinus(), BigInteger("199999999999999999999999999999999999999999998"))
    }

    @Test
    fun multiply() {
        //Zero
        verify(i0 * i0, 0)
        verify(i0 * i1, 0)
        verify(i1 * i0, 0)

        //Basic
        verify(i1 * i1, 1)
        verify(i3 * i5, 15)

        //Negative
        verify(neg * i5, -85)
        verify(i5 * neg, -85)
        verify(neg * neg, 289)

        //Big
        verify(big * big, BigInteger("9999999999999999999999999999999999999999999800000000000000000000000000000000000000000001"))
        verify(big * i1, BigInteger("99999999999999999999999999999999999999999999"))
        verify(i1 * big, BigInteger("99999999999999999999999999999999999999999999"))
        verify(big * big.unaryMinus(), BigInteger("-9999999999999999999999999999999999999999999800000000000000000000000000000000000000000001"))
    }

    @Test
    fun divide() {
        assertEquals(Integer(5), Integer(10) / two)
        assertEquals(Integer(20), Integer(10) / Rational(1,2))
        assertEquals(Rational(21, 4), Integer(3) / Rational(4,7))
        assertEquals(RealDouble(4.273504273504273), Integer(10) / RealDouble(2.34))
        assertEquals(RealBigDec("4.27"), Integer(10) / RealBigDec("2.34"))
        assertEquals(Integer(2), Integer(10) / Complex(5,0))
        assertEquals(Complex(Rational(8,13),Rational(-12,13)), Integer(4) / Complex(2,3))
    }

    @Test
    fun divideByZero() {
        assertFailsWith<ArithmeticException> { one / zero}
        assertFailsWith<ArithmeticException> { one / Rational(zero, one)}
        assertFailsWith<ArithmeticException> { one / RealDouble(0.0) }
        assertFailsWith<ArithmeticException> { one / Complex()}
    }

    @Test
    fun inc_dec() {
        var n = Integer.NEGATIVE_ONE
        assertEquals(Integer.ZERO, ++n)
        assertEquals(Integer.ONE, ++n)
        assertEquals(Integer.TWO, ++n)

        assertEquals(Integer.ONE, --n)
        assertEquals(Integer.ZERO, --n)
        assertEquals(Integer.NEGATIVE_ONE, --n)
    }

    @Test
    fun compareTo() {
        assertEquals(0, Integer(5).compareTo(Integer(5)))
        assertEquals(-1, Integer(5).compareTo(Integer(6)))
        assertEquals(1, Integer(6).compareTo(Integer(5)))

        assertEquals(0, zero.compareTo(Rational(0,1)))
        assertEquals(-1, Integer(5).compareTo(Rational(11,2)))
        assertEquals(1, Integer(5).compareTo(Rational(9,2)))

        assertEquals(0, Integer(5).compareTo(RealDouble(5.0)))
        assertEquals(-1, Integer(5).compareTo(RealDouble(5.00000001)))
        assertEquals(1, Integer(5).compareTo(RealDouble(4.999999999)))

        assertEquals(0, Integer.TWO.compareTo(Complex(Integer.TWO, zero)))
    }

    @Test
    fun compareTo_LargeNumbers() {
        val a = Integer("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999998")
        val b = Integer("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999")
        assertEquals(1, b.compareTo(a))
        assertEquals(1, one.compareTo(Rational(one,a)))
        assertEquals(-1, zero.compareTo(Rational(one,a)))
        assertEquals(1, a.compareTo(Rational(b, Integer(7))))
    }

    @Test
    fun compareTo_complex() {
        assertFailsWith<UnsupportedOperationException> { one.compareTo(Complex(zero, one)) }
    }

    @Test
    fun pow() {
        assertEquals(Integer.ONE, Integer.ONE.pow(Integer(-2)))
        assertEquals(Integer(-243), Integer(-3).pow(Integer(5)))
        assertEquals(Integer("910043815000214977332758527534256632492715260325658624"), Integer(12).pow(Integer(50)))
        assertEquals(Rational(Integer.ONE, Integer(243)), Integer(3).pow(Integer(-5)))
    }

    @Test
    fun intValue() {
        assertEquals(Int.MAX_VALUE, Integer("2147483647").intValue())
        assertEquals(Int.MIN_VALUE, Integer("-2147483648").intValue())
        assertFailsWith<OperationException> { Integer("2147483648").intValue() }
        assertFailsWith<OperationException> { Integer("-2147483649").intValue() }
    }

    private fun verify(e: NumberExpr, expected: Long) {
        assertEquals(NumberType.INTEGER, e.numType, "unexpected type: " + e.numType)

        val n = e as Integer
        assertEquals(expected, n.intValue().toLong())
    }

    private fun verify(e: NumberExpr, expected: BigInteger) {
        assertEquals(NumberType.INTEGER, e.numType, "unexpected type: " + e.numType)

        val n = e as Integer
        assertEquals(expected, n.toBigInteger())
    }

    companion object {
        val zero = Integer.ZERO
        val one = Integer.ONE
        val two = Integer.TWO
    }
}
