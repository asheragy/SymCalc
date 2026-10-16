package org.cerion.symcalc.function.core

import org.cerion.symcalc.EvalContext
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals

class SetTest {

    @Test
    fun assign() {
        val ctx = EvalContext()
        assertEquals(Integer(1), Set(VarExpr("x"), Integer(1)).eval(ctx))
        assertEquals(Integer(1), VarExpr("x").eval(ctx))
    }

    @Test
    fun reassign() {
        // Requires HoldFirst: without it the second Set evaluates its left side to 1 and fails validation
        val ctx = EvalContext()
        Set(VarExpr("x"), Integer(1)).eval(ctx)
        assertEquals(Integer(2), Set(VarExpr("x"), Integer(2)).eval(ctx))
        assertEquals(Integer(2), VarExpr("x").eval(ctx))
    }

    @Test
    fun rhsIsEvaluatedBeforeAssign() {
        val ctx = EvalContext()
        Set(VarExpr("x"), Integer(1)).eval(ctx)
        Set(VarExpr("y"), VarExpr("x")).eval(ctx)
        Set(VarExpr("x"), Integer(5)).eval(ctx)
        assertEquals(Integer(1), VarExpr("y").eval(ctx))
    }
}
