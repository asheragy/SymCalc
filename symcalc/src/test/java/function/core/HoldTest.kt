package org.cerion.symcalc.function.core

import org.cerion.symcalc.EvalContext
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals

class HoldTest {

    @Test
    fun basicEval() {
        assertEquals(Hold(Plus(Integer.ONE, Integer.ONE)), Hold(Plus(Integer.ONE, Integer.ONE)).eval())
    }

    @Test
    fun varExprNotEvaluated() {
        val e: Expr = Hold(Plus(VarExpr("x"), VarExpr("x")))
        val ctx = EvalContext()
        ctx["x"] = Integer(5)

        assertEquals(e, e.eval(ctx))
    }

    @Test
    fun flatPropertyHeld() {
        assertEquals(Hold(Plus(Integer.ONE, Plus(Integer.TWO, Integer.ZERO))), Hold(Plus(Integer.ONE, Plus(Integer.TWO, Integer.ZERO))).eval())
    }
}
