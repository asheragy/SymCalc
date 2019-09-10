package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.function.list.Join
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SinTest {

    @Test
    fun delayEval() {
        // Eval does nothing
        assertEquals(Sin(Integer(5)), Sin(Integer(5)).eval())

        // Evals to number
        assertEquals(RealDouble(-0.9589242746631385), N(Sin(Integer(5))).eval())
    }

    @Test
    fun basicPi() {
        assertEquals(Integer.ZERO, Sin(Integer.ZERO).eval())
        assertEquals(Integer.ONE, Sin(Divide(Pi(), Integer.TWO)).eval())
        assertEquals(Integer.ONE, Sin(Times(Pi(), Rational(1,2))).eval())
        assertEquals(Integer.ZERO, Sin(Pi()).eval())
        assertEquals(Integer.NEGATIVE_ONE, Sin(Times(Pi(), Rational(3,2))).eval())
        assertEquals(Integer.ZERO, Sin(Times(Pi(),Integer.TWO)).eval())
    }

    @Test
    fun valuesNotEvaluated() {
        val e = Sin(Times(Pi(), Rational(1,5)))
        assertEquals(e, e.eval())

        assertEquals(Sin(Times(Integer(3), Power(Integer.TWO, Rational.HALF))), Sin(Times(Integer(3), Power(Integer.TWO, Rational.HALF))).eval())
    }

    @Test
    fun basicPiCycles_over2() {
        // Test cycles with increments of Pi / 2
        val expected = ListExpr(Integer.ZERO, Integer.ONE, Integer.ZERO, Integer.NEGATIVE_ONE)

        val step = Times(Pi(), Rational(1,2))
        for(i in -10 until 10) {
            val sin = Sin(Times(Integer(i), step))
            val pos = (((i % 4) + 4) % 4) // mod but handles negative values
            assertEquals(expected[pos], sin.eval())
        }
    }

    @Test
    fun basicPiCycles_over3() {
        // Test cycles with increments of Pi / 3
        val values = ListExpr(Integer.ZERO, sqrt3Over2, sqrt3Over2)
        val negativeValues = Minus(values).eval()
        val expected = Join(values, negativeValues).eval()

        val step = Times(Pi(), Rational(1,3))
        for(i in -15 until 15) {
            val sin = Sin(Times(Integer(i), step))
            val pos = (((i % 6) + 6) % 6) // mod but handles negative values
            assertEquals(expected[pos], sin.eval())
        }
    }

    @Test
    fun basicPiCycles_over4() {
        // Test cycles with increments of Pi / 4
        val values = ListExpr(Integer.ZERO, oneOverSqrt2, Integer.ONE, oneOverSqrt2)
        val negativeValues = Minus(values).eval()
        val expected = Join(values, negativeValues).eval()

        val step = Times(Pi(), Rational(1,4))
        for(i in -20 until 20) {
            val x = Times(Integer(i), step).eval()
            val sin = Sin(x)
            val pos = (((i % 8) + 8) % 8) // mod but handles negative values
            assertEquals(expected[pos], sin.eval(), "$x")
        }
    }

    @Test
    fun basicPiCycles_over6() {
        // Test cycles with increments of Pi / 6
        val values = ListExpr(Integer.ZERO, Rational(1,2), sqrt3Over2, Integer.ONE, sqrt3Over2, Rational(1,2))
        val negativeValues = Minus(values).eval()
        val expected = Join(values, negativeValues).eval()

        val step = Times(Pi(), Rational(1,6))
        for(i in -30 until 30) {
            val x = Times(Integer(i), step).eval()
            val sin = Sin(x)
            val pos = (((i % 12) + 12) % 12) // mod but handles negative values
            assertEquals(expected[pos], sin.eval(), "$x")
        }
    }

    @Test
    fun bigDecimal() {
        assertEquals(RealBigDec("0.93"), Sin(RealBigDec("1.2")).eval())
        assertEquals(RealBigDec("0.72066"), Sin(RealBigDec("0.80475")).eval())
        assertEquals(RealBigDec("-0.958921"), Sin(RealBigDec("5.00001")).eval())

        assertEquals(RealBigDec("0.84147098480789650665250232163029899962256306079837"), Sin(RealBigDec("1.0000000000000000000000000000000000000000000000000")).eval())
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }
}