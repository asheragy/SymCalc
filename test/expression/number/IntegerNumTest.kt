package org.cerion.symcalc.expression.number

import org.junit.Test

import java.math.BigInteger

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import java.math.BigDecimal

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
        verify(-IntegerNum.ZERO, 0)
        verify(-IntegerNum.ONE, -1)
        verify(-IntegerNum.TWO, -2)
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
        //Zero
        divideByZero(i1, i0)
        divideByZero(i1, RationalNum(i0, i1))
        divideByZero(i1, RealNum.create(0.0))
        divideByZero(i1, ComplexNum())

        //Add more later
    }

    @Test
    fun power_realAndComplexConverted() {
        // Integer is converted to these values and power() is evaluated in those classes, minimal testing needed for this class
        val three = IntegerNum(3)
        val real = RealNum.create(3.14)
        val realBig = RealNum.create(BigDecimal("123.35340583128859694839201928385968473749596868726265"))
        val complex = ComplexNum(2,3)

        assertEquals(RealNum.create(3.0).power(real), three.power(real))
        assertEquals(RealNum.create(3.0).power(realBig), three.power(realBig))
        //assertEquals(ComplexNum(3,0).power(complex), three.power(complex))
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
}
