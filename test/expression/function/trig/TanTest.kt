package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.function.list.Join
import org.cerion.symcalc.expression.number.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TanTest {

    @Test
    fun basic() {
        val tan = Tan(Integer(5))
        var eval = tan.eval()

        // Eval does nothing
        assertEquals(tan, eval)

        // Evals to number
        val tan2 = N(tan)
        eval = tan2.eval()
        assertTrue(eval is NumberExpr, "not a number")

        assertEquals(RealDouble(-3.380515006246586), eval)
    }

    @Test
    fun basicPiCycles_over2() {
        // Test cycles with increments of Pi / 2
        val expected = ListExpr(Integer.ZERO, ComplexInfinity(), Integer.ZERO, ComplexInfinity())

        val step = Times(Pi(), Rational(1,2))
        for(i in -10 until 10) {
            val tan = Tan(Times(Integer(i), step))
            val pos = (((i % 4) + 4) % 4) // mod but handles negative values
            assertEquals(expected[pos], tan.eval())
        }
    }

    @Test
    fun basicPiCycles_over3() {
        // Test cycles with increments of Pi / 3
        val values = ListExpr(Integer.ZERO, sqrt3, Minus(sqrt3))
        val expected = Join(values, values).eval()

        val step = Times(Pi(), Rational(1,3))
        for(i in -15 until 15) {
            val tan = Tan(Times(Integer(i), step))
            val pos = (((i % 6) + 6) % 6) // mod but handles negative values
            assertEquals(expected[pos], tan.eval())
        }
    }

    @Test
    fun basicPiCycles_over4() {
        // Test cycles with increments of Pi / 4
        val values = ListExpr(Integer.ZERO, Integer.ONE, ComplexInfinity(), Integer.NEGATIVE_ONE)
        val expected = Join(values, values).eval()

        val step = Times(Pi(), Rational(1,4))
        for(i in -20 until 20) {
            val x = Times(Integer(i), step).eval()
            val tan = Tan(x)
            val pos = (((i % 8) + 8) % 8) // mod but handles negative values
            assertEquals(expected[pos], tan.eval(), "$x")
        }
    }

    @Test
    fun basicPiCycles_over6() {
        // Test cycles with increments of Pi / 6
        val values = ListExpr(Integer.ZERO, oneOverSqrt3, sqrt3, ComplexInfinity(), Minus(sqrt3), Minus(oneOverSqrt3))
        val expected = Join(values, values).eval()

        val step = Times(Pi(), Rational(1,6))
        for(i in -30 until 30) {
            val x = Times(Integer(i), step).eval()
            val tan = Tan(x)
            val pos = (((i % 12) + 12) % 12) // mod but handles negative values
            assertEquals(expected[pos], tan.eval(), "$x")
        }
    }

    @Test
    fun bigDecimal() {
        //assertEquals(RealBigDec("1.5574"), Tan(RealBigDec("1.0000")).eval())
        //assertEquals(RealBigDec("3.38045"), Tan(RealBigDec("1.28318")).eval())
        // TODO_LP not quite right, also need to work on performance
        //assertEquals(RealBigDec("-3.3804"), Tan(RealBigDec("5.00001")).eval())
    }

    companion object {
        val sqrt3 = Power(Integer(3), Rational(1,2))
        val oneOverSqrt3 = Power(Integer(3), Rational(-1,2))
    }
}

