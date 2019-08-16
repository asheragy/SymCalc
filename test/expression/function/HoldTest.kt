package org.cerion.symcalc.expression.function

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.core.Hold
import org.cerion.symcalc.expression.number.Integer
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class HoldTest {

    @Test
    fun basicEval() {
        var e: Expr = Hold(Plus(Integer.ONE, Integer.ONE))
        e = e.eval()

        assertTrue(e.isFunction("hold"))
        assertEquals(1, e.size.toLong())
        assertTrue(e[0].isFunction("plus"))
    }

    @Test
    fun varExprNotEvaluated() {
        var e: Expr = Hold(Plus(VarExpr("x"), VarExpr("x")))
        e.env.setVar("x", Integer(5))

        // Verify plus is not evaluated and is x+x, not 5+5
        e = e.eval()[0][0]
        assertEquals(VarExpr("x"), e)
    }
}