package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import kotlin.test.Test
import kotlin.test.assertEquals

class CosTest {

    @Test
    fun basicPiCycles_over2() {
        val expected = ListExpr(1, 0, -1, 0)
        assertTrigExprRange(expected, SymbolExpr("cos"))
    }

    @Test
    fun basicPiCycles_over3() {
        val values = ListExpr(1, Rational.HALF, Rational.HALF.unaryMinus())
        val negativeValues = Minus(values).eval() as ListExpr

        assertTrigExprRange(values.join(negativeValues), SymbolExpr("cos"))
    }

    @Test
    fun basicPiCycles_over4() {
        val values = ListExpr(1, oneOverSqrt2, 0, Minus(oneOverSqrt2))
        val negativeValues = Minus(values).eval() as ListExpr

        assertTrigExprRange(values.join(negativeValues), SymbolExpr("cos"))
    }

    @Test
    fun basicPiCycles_over6() {
        val values = ListExpr(1, sqrt3Over2, Rational.HALF, 0, Rational.HALF.unaryMinus(), Minus(sqrt3Over2))
        val negativeValues = Minus(values).eval() as ListExpr

        assertTrigExprRange(values.join(negativeValues), SymbolExpr("cos"))
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