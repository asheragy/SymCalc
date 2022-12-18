package org.cerion.symcalc.function.trig

import org.cerion.symcalc.`==`
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
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
        Cos("1.2") `==` "0.36"
        Cos("0.80475") `==` "0.69329"
        Cos("5.00001") `==` "0.283672"

        Cos("1.0000000000000000000000000000000000000000000000000") `==` "0.54030230586813971740093660744297660373231042061792"
    }

    @Test
    fun precision() {
        val a = Cos(RealBigDec("1.22222222")).eval() as RealBigDec
        assertEquals(9, a.precision)
        assertEquals(19, a.value.precision())

        val b = Cos(RealBigDec("1.222222222")).eval() as RealBigDec
        assertEquals(10, b.precision)
        assertEquals(28, b.value.precision())
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }
}