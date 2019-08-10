package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.list.Join
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import org.junit.Assert
import org.junit.Test

class CosTest {

    @Test
    fun basicPiCycles_over2() {
        // Test cycles with increments of Pi / 2
        val expected = ListExpr(IntegerNum.ONE, IntegerNum.ZERO, IntegerNum.NEGATIVE_ONE, IntegerNum.ZERO)

        val step = Times(Pi(), Rational(1,2))
        for(i in -10 until 10) {
            val cos = Cos(Times(IntegerNum(i), step))
            val pos = (((i % 4) + 4) % 4) // mod but handles negative values
            Assert.assertEquals(expected[pos], cos.eval())
        }
    }

    @Test
    fun basicPiCycles_over3() {
        // Test cycles with increments of Pi / 3
        val values = ListExpr(IntegerNum.ONE, Rational.HALF, Rational.HALF.unaryMinus())
        val negativeValues = Minus(values)
        val expected = Join(values, negativeValues).eval()

        val step = Times(Pi(), Rational(1,3))
        for(i in -15 until 15) {
            val cos = Cos(Times(IntegerNum(i), step))
            val pos = (((i % 6) + 6) % 6) // mod but handles negative values
            Assert.assertEquals(expected[pos], cos.eval())
        }
    }

    @Test
    fun basicPiCycles_over4() {
        // Test cycles with increments of Pi / 4
        val values = ListExpr(IntegerNum.ONE, oneOverSqrt2, IntegerNum.ZERO, Minus(oneOverSqrt2))
        val negativeValues = Minus(values)//.eval()
        val expected = Join(values, negativeValues).eval()

        val step = Times(Pi(), Rational(1,4))
        for(i in -20 until 20) {
            val x = Times(IntegerNum(i), step).eval()
            val cos = Cos(x)
            val pos = (((i % 8) + 8) % 8) // mod but handles negative values
            Assert.assertEquals("$x", expected[pos], cos.eval())
        }
    }

    @Test
    fun basicPiCycles_over6() {
        // Test cycles with increments of Pi / 6
        val values = ListExpr(IntegerNum.ONE, sqrt3Over2, Rational.HALF, IntegerNum.ZERO, Rational.HALF.unaryMinus(), Minus(sqrt3Over2))
        val negativeValues = Minus(values)
        val expected = Join(values, negativeValues).eval()

        val step = Times(Pi(), Rational(1,6))
        for(i in -30 until 30) {
            val x = Times(IntegerNum(i), step).eval()
            val cos = Cos(x)
            val pos = (((i % 12) + 12) % 12) // mod but handles negative values
            Assert.assertEquals("$x", expected[pos], cos.eval())
        }
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(IntegerNum(3), Rational(1,2)))
        val oneOverSqrt2 = Power(IntegerNum.TWO, Rational(-1,2))
    }
}