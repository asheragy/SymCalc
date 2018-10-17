package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.NumberExpr
import org.junit.Test

import java.math.BigInteger

import org.junit.Assert.assertEquals
import org.junit.Assert.fail

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
        verify(IntegerNum.ZERO.negate(), 0)
        verify(IntegerNum.ONE.negate(), -1)
        verify(IntegerNum.TWO.negate(), -2)
        verify(IntegerNum(-5).negate(), 5)
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
        verify(big + big.negate(), 0)
    }

    @Test
    fun subtraction() {
        //Zero
        verify(i0.subtract(i0), 0)
        verify(i0.subtract(i1), -1)
        verify(i1.subtract(i0), 1)

        //Basic
        verify(i1.subtract(i1), 0)
        verify(i3.subtract(i5), -2)
        verify(i5.subtract(i3), 2)

        //Negative
        verify(neg.subtract(i5), -22)
        verify(i5.subtract(neg), 22)
        verify(neg.subtract(neg), 0)

        //Big
        verify(big.subtract(big), BigInteger("0"))
        verify(big.subtract(i1), BigInteger("99999999999999999999999999999999999999999998"))
        verify(i1.subtract(big), BigInteger("-99999999999999999999999999999999999999999998"))
        verify(big.subtract(big.negate()), BigInteger("199999999999999999999999999999999999999999998"))
    }

    @Test
    fun multiply() {
        //Zero
        verify(i0.multiply(i0), 0)
        verify(i0.multiply(i1), 0)
        verify(i1.multiply(i0), 0)

        //Basic
        verify(i1.multiply(i1), 1)
        verify(i3.multiply(i5), 15)

        //Negative
        verify(neg.multiply(i5), -85)
        verify(i5.multiply(neg), -85)
        verify(neg.multiply(neg), 289)

        //Big
        verify(big.multiply(big), BigInteger("9999999999999999999999999999999999999999999800000000000000000000000000000000000000000001"))
        verify(big.multiply(i1), BigInteger("99999999999999999999999999999999999999999999"))
        verify(i1.multiply(big), BigInteger("99999999999999999999999999999999999999999999"))
        verify(big.multiply(big.negate()), BigInteger("-9999999999999999999999999999999999999999999800000000000000000000000000000000000000000001"))
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

    private fun verify(e: NumberExpr, expected: Long) {
        if (e.numType() != NumberExpr.INTEGER)
            fail("unexpected type: " + e.numType())

        val n = e as IntegerNum
        assertEquals(expected, n.intValue().toLong())
    }

    private fun verify(e: NumberExpr, expected: BigInteger) {
        if (e.numType() != NumberExpr.INTEGER)
            fail("unexpected type: " + e.numType())

        val n = e as IntegerNum
        assertEquals(expected, n.toBigInteger())
    }

    private fun divideByZero(n: NumberExpr, exp: NumberExpr) {
        try {
            verify(n.divide(exp), 0)
        } catch (e: ArithmeticException) {
            //Success
        }

    }

}
