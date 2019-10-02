package expression.function.trig

import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.list.Join
import org.cerion.symcalc.expression.function.trig.Cot
import org.cerion.symcalc.expression.number.Complex
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

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
        // Test cycles with increments of Pi / 2
        val expected = ListExpr(ComplexInfinity(), 0, ComplexInfinity(), 0)

        val step = Times(Pi(), Rational(1,2))
        for(i in -10 until 10) {
            val cot = Cot(Times(Integer(i), step))
            val pos = (((i % 4) + 4) % 4) // mod but handles negative values
            assertEquals(expected[pos], cot.eval())
        }
    }

    @Test
    fun basicPiCycles_over3() {
        // Test cycles with increments of Pi / 3
        val values = ListExpr(ComplexInfinity(), oneOverSqrt3, Minus(oneOverSqrt3))
        val expected = Join(values, values).eval() as ListExpr

        val step = Times(Pi(), Rational(1,3))
        for(i in -15 until 15) {
            val cot = Cot(Times(Integer(i), step))
            val pos = (((i % 6) + 6) % 6) // mod but handles negative values
            assertEquals(expected[pos], cot.eval())
        }
    }

    @Test
    fun basicPiCycles_over4() {
        // Test cycles with increments of Pi / 4
        val values = ListExpr(ComplexInfinity(), 1, 0, -1)
        val expected = Join(values, values).eval() as ListExpr

        val step = Times(Pi(), Rational(1,4))
        for(i in -20 until 20) {
            val x = Times(Integer(i), step).eval()
            val cot = Cot(x)
            val pos = (((i % 8) + 8) % 8) // mod but handles negative values
            assertEquals(expected[pos], cot.eval(), "$x")
        }
    }

    @Test
    fun basicPiCycles_over6() {
        // Test cycles with increments of Pi / 6
        val values = ListExpr(ComplexInfinity(), sqrt3, oneOverSqrt3, 0, Minus(oneOverSqrt3), Minus(sqrt3))
        val expected = Join(values, values).eval() as ListExpr

        val step = Times(Pi(), Rational(1,6))
        for(i in -30 until 30) {
            val x = Times(Integer(i), step).eval()
            val cot = Cot(x)
            val pos = (((i % 12) + 12) % 12) // mod but handles negative values
            assertEquals(expected[pos], cot.eval(), "$x")
        }
    }

    companion object {
        val sqrt3 = Power(Integer(3), Rational(1,2))
        val oneOverSqrt3 = Power(Integer(3), Rational(-1,2))
    }
}