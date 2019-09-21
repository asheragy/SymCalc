package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.list.Join
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CosTest {

    @Test
    fun basicPiCycles_over2() {
        // Test cycles with increments of Pi / 2
        val expected = ListExpr(Integer.ONE, Integer.ZERO, Integer.NEGATIVE_ONE, Integer.ZERO)

        val step = Times(Pi(), Rational(1,2))
        for(i in -10 until 10) {
            val cos = Cos(Times(Integer(i), step))
            val pos = (((i % 4) + 4) % 4) // mod but handles negative values
            assertEquals(expected[pos], cos.eval())
        }
    }

    @Test
    fun basicPiCycles_over3() {
        // Test cycles with increments of Pi / 3
        val values = ListExpr(Integer.ONE, Rational.HALF, Rational.HALF.unaryMinus())
        val negativeValues = Minus(values)
        val expected = Join(values, negativeValues).eval() as ListExpr

        val step = Times(Pi(), Rational(1,3))
        for(i in -15 until 15) {
            val cos = Cos(Times(Integer(i), step))
            val pos = (((i % 6) + 6) % 6) // mod but handles negative values
            assertEquals(expected[pos], cos.eval())
        }
    }

    @Test
    fun basicPiCycles_over4() {
        // Test cycles with increments of Pi / 4
        val values = ListExpr(Integer.ONE, oneOverSqrt2, Integer.ZERO, Minus(oneOverSqrt2))
        val negativeValues = Minus(values)//.eval()
        val expected = Join(values, negativeValues).eval() as ListExpr

        val step = Times(Pi(), Rational(1,4))
        for(i in -20 until 20) {
            val x = Times(Integer(i), step).eval()
            val cos = Cos(x)
            val pos = (((i % 8) + 8) % 8) // mod but handles negative values
            assertEquals(expected[pos], cos.eval(), "$x")
        }
    }

    @Test
    fun basicPiCycles_over6() {
        // Test cycles with increments of Pi / 6
        val values = ListExpr(Integer.ONE, sqrt3Over2, Rational.HALF, Integer.ZERO, Rational.HALF.unaryMinus(), Minus(sqrt3Over2))
        val negativeValues = Minus(values)
        val expected = Join(values, negativeValues).eval() as ListExpr

        val step = Times(Pi(), Rational(1,6))
        for(i in -30 until 30) {
            val x = Times(Integer(i), step).eval()
            val cos = Cos(x)
            val pos = (((i % 12) + 12) % 12) // mod but handles negative values
            assertEquals(expected[pos], cos.eval(), "$x")
        }
    }

    @Test
    fun bigDecimal() {
        assertEquals(RealBigDec("0.36"), Cos(RealBigDec("1.2")).eval())
        assertEquals(RealBigDec("0.69329"), Cos(RealBigDec("0.80475")).eval())
        assertEquals(RealBigDec("0.283672"), Cos(RealBigDec("5.00001")).eval())

        assertEquals(RealBigDec("0.54030230586813971740093660744297660373231042061792"), Cos(RealBigDec("1.0000000000000000000000000000000000000000000000000")).eval())
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }
}