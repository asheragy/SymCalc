package expression.function.trig

import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.list.Join
import org.cerion.symcalc.expression.function.trig.Cot
import org.cerion.symcalc.expression.function.trig.assertTrigExprRange
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import kotlin.test.Test

internal class CotTest {

    @Test
    fun basic() {
        Cot(1.0).eval() `should equal` 0.6420926159343306
    }

    @Test
    fun bigDec() {
        Cot("1.0000").eval() `should equal` "0.64209"
        Cot("2.0000000000000000001").eval() `should equal` "-0.45765755436028576387"
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