package org.cerion.symcalc.expression.number

import org.cerion.symcalc.exception.OperationException
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.math.BigInteger
import kotlin.test.assertFailsWith

class IntegerNumTest {

    private val i0 = IntegerNum(0)
    private val i1 = IntegerNum(1)
    private val i3 = IntegerNum(3)
    private val i5 = IntegerNum(5)
    private val neg = IntegerNum(-17)
    private val big = IntegerNum("99999999999999999999999999999999999999999999")

    @Test
    fun stringConstructor() {
        verify(IntegerNum("0"), 0)
        verify(IntegerNum("5"), 5)
        verify(IntegerNum("-5"), -5)
        verify(IntegerNum("1234567890"), 1234567890)
        verify(IntegerNum("-1234567890"), -1234567890)

        verify(IntegerNum("9999999999999999999999999999999999999999"), BigInteger("9999999999999999999999999999999999999999"))
    }

    @Test
    fun constants() {
        verify(IntegerNum.ZERO, 0)
        verify(IntegerNum.ONE, 1)
        verify(IntegerNum.TWO, 2)
    }

    @Test
    fun negate() {
        verify(-zero, 0)
        verify(-one, -1)
        verify(-two, -2)
        verify(-IntegerNum(-5), 5)
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
        assertEquals(IntegerNum(5), IntegerNum(10) / two)
        assertEquals(IntegerNum(20), IntegerNum(10) / Rational(1,2))
        assertEquals(Rational(21, 4), IntegerNum(3) / Rational(4,7))
        assertEquals(RealNum_Double(4.273504273504273), IntegerNum(10) / RealNum_Double(2.34))
        assertEquals(RealNum_BigDecimal("4.27"), IntegerNum(10) / RealNum_BigDecimal("2.34"))
        assertEquals(IntegerNum(2), IntegerNum(10) / Complex(5,0))
        assertEquals(Complex(Rational(8,13),Rational(-12,13)), IntegerNum(4) / Complex(2,3))
    }

    @Test
    fun divideByZero() {
        assertFailsWith<ArithmeticException> { one / zero}
        assertFailsWith<ArithmeticException> { one / Rational(zero, one)}
        assertFailsWith<ArithmeticException> { one / RealNum.create(0.0)}
        assertFailsWith<ArithmeticException> { one / Complex()}
    }

    @Test
    fun inc_dec() {
        var n = IntegerNum.NEGATIVE_ONE
        assertEquals(IntegerNum.ZERO, ++n)
        assertEquals(IntegerNum.ONE, ++n)
        assertEquals(IntegerNum.TWO, ++n)

        assertEquals(IntegerNum.ONE, --n)
        assertEquals(IntegerNum.ZERO, --n)
        assertEquals(IntegerNum.NEGATIVE_ONE, --n)
    }

    @Test
    fun compareTo() {
        assertEquals(0, IntegerNum(5).compareTo(IntegerNum(5)))
        assertEquals(-1, IntegerNum(5).compareTo(IntegerNum(6)))
        assertEquals(1, IntegerNum(6).compareTo(IntegerNum(5)))

        assertEquals(0, zero.compareTo(Rational(0,1)))
        assertEquals(-1, IntegerNum(5).compareTo(Rational(11,2)))
        assertEquals(1, IntegerNum(5).compareTo(Rational(9,2)))

        assertEquals(0, IntegerNum(5).compareTo(RealNum.create(5.0)))
        assertEquals(-1, IntegerNum(5).compareTo(RealNum.create(5.00000001)))
        assertEquals(1, IntegerNum(5).compareTo(RealNum.create(4.999999999)))

        assertEquals(0, IntegerNum.TWO.compareTo(Complex(IntegerNum.TWO, zero)))
    }

    @Test
    fun compareTo_LargeNumbers() {
        val a = IntegerNum("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999998")
        val b = IntegerNum("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999")
        assertEquals(1, b.compareTo(a))
        assertEquals(1, one.compareTo(Rational(one,a)))
        assertEquals(-1, zero.compareTo(Rational(one,a)))
        assertEquals(1, a.compareTo(Rational(b,IntegerNum(7))))
    }

    @Test
    fun compareTo_complex() {
        assertFailsWith<UnsupportedOperationException> { one.compareTo(Complex(zero, one)) }
    }

    @Test
    fun pow() {
        assertEquals(IntegerNum.ONE, IntegerNum.ONE.pow(IntegerNum(-2)))
        assertEquals(IntegerNum(-243), IntegerNum(-3).pow(IntegerNum(5)))
        assertEquals(IntegerNum("910043815000214977332758527534256632492715260325658624"), IntegerNum(12).pow(IntegerNum(50)))
        assertEquals(Rational(IntegerNum.ONE, IntegerNum(243)), IntegerNum(3).pow(IntegerNum(-5)))
    }

    @Test
    fun intValue() {
        assertEquals(Int.MAX_VALUE, IntegerNum("2147483647").intValue())
        assertEquals(Int.MIN_VALUE, IntegerNum("-2147483648").intValue())
        assertFailsWith<OperationException> { IntegerNum("2147483648").intValue() }
        assertFailsWith<OperationException> { IntegerNum("-2147483649").intValue() }
    }

    private fun verify(e: NumberExpr, expected: Long) {
        if (e.numType != NumberType.INTEGER)
            fail("unexpected type: " + e.numType)

        val n = e as IntegerNum
        assertEquals(expected, n.intValue().toLong())
    }

    private fun verify(e: NumberExpr, expected: BigInteger) {
        if (e.numType != NumberType.INTEGER)
            fail("unexpected type: " + e.numType)

        val n = e as IntegerNum
        assertEquals(expected, n.toBigInteger())
    }

    private fun divideByZero(n: NumberExpr, exp: NumberExpr) {
        try {
            verify(n.div(exp), 0)
            assert(true)
        } catch (e: ArithmeticException) {
            //Success
        }
    }

    companion object {
        val zero = IntegerNum.ZERO
        val one = IntegerNum.ONE
        val two = IntegerNum.TWO
    }
}
