package org.cerion.symcalc.function.trig

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.core.N
import org.cerion.symcalc.expression.number.*
import kotlin.test.Test
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
        val expected = ListExpr(0, ComplexInfinity(), 0, ComplexInfinity())
        assertTrigExprRange(expected, SymbolExpr("tan"))
    }

    @Test
    fun basicPiCycles_over3() {
        val values = ListExpr(0, sqrt3, Minus(sqrt3))
        val expected = values.join(values)

        assertTrigExprRange(expected, SymbolExpr("tan"))
    }

    @Test
    fun basicPiCycles_over4() {
        val values = ListExpr(0, 1, ComplexInfinity(), -1)
        val expected = values.join(values)

        assertTrigExprRange(expected, SymbolExpr("tan"))
    }

    @Test
    fun basicPiCycles_over6() {
        val values = ListExpr(0, oneOverSqrt3, sqrt3, ComplexInfinity(), Minus(sqrt3), Minus(oneOverSqrt3))
        val expected = values.join(values)

        assertTrigExprRange(expected, SymbolExpr("tan"))
    }

    @Test
    fun bigDecimal() {
        assertEquals(RealBigDec("1.5574"), Tan(RealBigDec("1.0000")).eval())
        assertEquals(RealBigDec("3.38045"), Tan(RealBigDec("1.28318")).eval())
        assertEquals(RealBigDec("-3.38039"), Tan(RealBigDec("5.00001")).eval())

        assertEquals(RealBigDec("1.1548660069442902748685813894288494284408800565653"), Tan(RealBigDec("0.85714285714285714285714285714285714285714285714286")).eval())
    }

    companion object {
        val sqrt3 = Power(Integer(3), Rational(1,2))
        val oneOverSqrt3 = Power(Integer(3), Rational(-1,2))
    }
}

