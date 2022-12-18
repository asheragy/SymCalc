package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.list.Join
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import kotlin.test.Test

internal class CotTest {

    @Test
    fun double() {
        Cot(1.0) `==` 0.6420926159343306
    }

    @Test
    fun bigDec() {
        Cot("1.0000") `==` "0.64209"
        Cot("2.0000000000000000001") `==` "-0.45765755436028576387"
    }

    @Test
    fun basicPiCycles_over2() {
        val expected = ListExpr(ComplexInfinity(), 0, ComplexInfinity(), 0)
        assertTrigExprRange(expected, SymbolExpr("cot"))
    }

    @Test
    fun basicPiCycles_over3() {
        val values = ListExpr(ComplexInfinity(), oneOverSqrt3, Minus(oneOverSqrt3))
        val expected = Join(values, values).eval() as ListExpr

        assertTrigExprRange(expected, SymbolExpr("cot"))
    }

    @Test
    fun basicPiCycles_over4() {
        val values = ListExpr(ComplexInfinity(), 1, 0, -1)
        val expected = Join(values, values).eval() as ListExpr

        assertTrigExprRange(expected, SymbolExpr("cot"))
    }

    @Test
    fun basicPiCycles_over6() {
        val values = ListExpr(ComplexInfinity(), sqrt3, oneOverSqrt3, 0, Minus(oneOverSqrt3), Minus(sqrt3))
        val expected = Join(values, values).eval() as ListExpr

        assertTrigExprRange(expected, SymbolExpr("cot"))
    }
    
    companion object {
        val sqrt3 = Power(Integer(3), Rational(1,2))
        val oneOverSqrt3 = Power(Integer(3), Rational(-1,2))
    }
}