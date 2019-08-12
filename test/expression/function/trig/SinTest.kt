package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.function.list.Join
import org.cerion.symcalc.expression.function.statistics.RandomInteger
import org.cerion.symcalc.expression.number.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SinTest {

    @Test
    fun delayEval() {
        // Eval does nothing
        assertEquals(Sin(IntegerNum(5)), Sin(IntegerNum(5)).eval())

        // Evals to number
        assertEquals(RealNum_Double(-0.9589242746631385), N(Sin(IntegerNum(5))).eval())
    }

    @Test
    fun listParameter() {
        val params = ListExpr(IntegerNum.ONE, VarExpr("x"), RandomInteger())
        val e = Sin(params).eval()

        assertTrue(e.isList)
        assertTrue(e[0].isFunction("sin"))
    }

    @Test
    fun basicPi() {
        assertEquals(IntegerNum.ZERO, Sin(IntegerNum.ZERO).eval())
        assertEquals(IntegerNum.ONE, Sin(Divide(Pi(), IntegerNum.TWO)).eval())
        assertEquals(IntegerNum.ONE, Sin(Times(Pi(), Rational(1,2))).eval())
        assertEquals(IntegerNum.ZERO, Sin(Pi()).eval())
        assertEquals(IntegerNum.NEGATIVE_ONE, Sin(Times(Pi(), Rational(3,2))).eval())
        assertEquals(IntegerNum.ZERO, Sin(Times(Pi(),IntegerNum.TWO)).eval())
    }

    @Test
    fun valuesNotEvaluated() {
        val e = Sin(Times(Pi(), Rational(1,5)))
        assertEquals(e, e.eval())

        assertEquals(Sin(Times(IntegerNum(3), Power(IntegerNum.TWO, Rational.HALF))), Sin(Times(IntegerNum(3), Power(IntegerNum.TWO, Rational.HALF))).eval())
    }

    @Test
    fun basicPiCycles_over2() {
        // Test cycles with increments of Pi / 2
        val expected = ListExpr(IntegerNum.ZERO, IntegerNum.ONE, IntegerNum.ZERO, IntegerNum.NEGATIVE_ONE)

        val step = Times(Pi(), Rational(1,2))
        for(i in -10 until 10) {
            val sin = Sin(Times(IntegerNum(i), step))
            val pos = (((i % 4) + 4) % 4) // mod but handles negative values
            assertEquals(expected[pos], sin.eval())
        }
    }

    @Test
    fun basicPiCycles_over3() {
        // Test cycles with increments of Pi / 3
        val values = ListExpr(IntegerNum.ZERO, sqrt3Over2, sqrt3Over2)
        val negativeValues = Minus(values).eval()
        val expected = Join(values, negativeValues).eval()

        val step = Times(Pi(), Rational(1,3))
        for(i in -15 until 15) {
            val sin = Sin(Times(IntegerNum(i), step))
            val pos = (((i % 6) + 6) % 6) // mod but handles negative values
            assertEquals(expected[pos], sin.eval())
        }
    }

    @Test
    fun basicPiCycles_over4() {
        // Test cycles with increments of Pi / 4
        val values = ListExpr(IntegerNum.ZERO, oneOverSqrt2, IntegerNum.ONE, oneOverSqrt2)
        val negativeValues = Minus(values).eval()
        val expected = Join(values, negativeValues).eval()

        val step = Times(Pi(), Rational(1,4))
        for(i in -20 until 20) {
            val x = Times(IntegerNum(i), step).eval()
            val sin = Sin(x)
            val pos = (((i % 8) + 8) % 8) // mod but handles negative values
            assertEquals("$x", expected[pos], sin.eval())
        }
    }

    @Test
    fun basicPiCycles_over6() {
        // Test cycles with increments of Pi / 6
        val values = ListExpr(IntegerNum.ZERO, Rational(1,2), sqrt3Over2, IntegerNum.ONE, sqrt3Over2, Rational(1,2))
        val negativeValues = Minus(values).eval()
        val expected = Join(values, negativeValues).eval()

        val step = Times(Pi(), Rational(1,6))
        for(i in -30 until 30) {
            val x = Times(IntegerNum(i), step).eval()
            val sin = Sin(x)
            val pos = (((i % 12) + 12) % 12) // mod but handles negative values
            assertEquals("$x", expected[pos], sin.eval())
        }
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(IntegerNum(3), Rational(1,2)))
        val oneOverSqrt2 = Power(IntegerNum.TWO, Rational(-1,2))
    }
}