package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.test.Test
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
        assertTrigExprRange(ListExpr(0, 1, 0, -1), SymbolExpr("sin"))
    }

    @Test
    fun basicPiCycles_over3() {
        val values = ListExpr(0, sqrt3Over2, sqrt3Over2)
        val negativeValues = Minus(values).eval() as ListExpr

        assertTrigExprRange(values.join(negativeValues), SymbolExpr("sin"))
    }

    @Test
    fun basicPiCycles_over4() {
        val values = ListExpr(0, oneOverSqrt2, 1, oneOverSqrt2)
        val negativeValues = Minus(values).eval() as ListExpr

        assertTrigExprRange(values.join(negativeValues), SymbolExpr("sin"))
    }

    @Test
    fun basicPiCycles_over6() {
        val values = ListExpr(0, Rational(1,2), sqrt3Over2, 1, sqrt3Over2, Rational(1,2))
        val negativeValues = Minus(values).eval() as ListExpr

        assertTrigExprRange(values.join(negativeValues), SymbolExpr("sin"))
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