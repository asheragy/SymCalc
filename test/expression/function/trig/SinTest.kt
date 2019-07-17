package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.function.statistics.RandomInteger
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SinTest {

    @Test
    fun delayEval() {
        val sin = Sin(IntegerNum(5))
        var eval = sin.eval()

        // Eval does nothing
        assertEquals(sin, eval)

        // Evals to number
        val sin2 = N(sin)
        eval = sin2.eval()
        assertTrue(eval.isNumber)

        val num = eval as RealNum
        assertEquals(-0.958924, num.toDouble(), 0.00001)
    }

    @Test
    fun listParameter() {
        val params = ListExpr(IntegerNum.ONE, VarExpr("x"), RandomInteger())
        val e = Sin(params).eval()

        assertTrue(e.isList)
        assertTrue(e[0].isFunction("sin"))
    }
}