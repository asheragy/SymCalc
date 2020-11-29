package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.Integer
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
        e.env.setVar("x", Integer(5))

        assertEquals(e, e.eval())
    }

    @Test
    fun flatPropertyHeld() {
        assertEquals(Hold(Plus(Integer.ONE, Plus(Integer.TWO, Integer.ZERO))), Hold(Plus(Integer.ONE, Plus(Integer.TWO, Integer.ZERO))).eval())
    }
}