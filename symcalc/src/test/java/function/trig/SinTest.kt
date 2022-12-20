package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.core.N
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import kotlin.test.Test
import kotlin.test.assertEquals

class SinTest {

    @Test
    fun delayEval() {
        // Eval does nothing
        Sin(5) `==` Sin(5)

        // Evals to number
        N(Sin(5)) `==` -0.9589242746631385
    }

    @Test
    fun basicPi() {
        Sin(0) `==` 0
        Sin(Divide(Pi(), 2)) `==` 1
        Sin(Times(Pi(), Rational(1,2))) `==` 1
        Sin(Pi()) `==` 0
        Sin(Times(Pi(), Rational(3,2))) `==` -1
        Sin(Times(Pi(), 2)) `==` 0
    }

    @Test
    fun valuesNotEvaluated() {
        Sin(Times(Pi(), Rational(1,5))) `==` Sin(Times(Pi(), Rational(1,5)))
        Sin(Times(3, Power(2, Rational.HALF))) `==` Sin(Times(3, Power(2, Rational.HALF)))
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

    // TODO more values https://en.wikipedia.org/wiki/Exact_trigonometric_values#Common_angles

    @Test
    fun bigDecimal() {
        Sin("1.2") `==` "0.93"
        Sin("0.80475") `==` "0.72066"
        Sin("5.00001") `==` "-0.958921"

        Sin("1.0000000000000000000000000000000000000000000000000") `==` "0.84147098480789650665250232163029899962256306079837"
    }

    @Test
    fun precision() {
        val a = Sin(RealBigDec("1.22222222")).eval() as RealBigDec
        assertEquals(9, a.precision)
        assertEquals(19, a.value.precision())

        val b = Sin(RealBigDec("1.222222222")).eval() as RealBigDec
        assertEquals(10, b.precision)
        assertEquals(28, b.value.precision())
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }
}