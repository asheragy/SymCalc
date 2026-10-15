package org.cerion.symcalc.function.procedural

import org.cerion.symcalc.EvalContext
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.core.Set
import org.cerion.symcalc.function.logical.Greater
import org.cerion.symcalc.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class IfTest {

    @Test
    fun basic() {
        var e: Expr = If(Greater(Integer(3), Integer(6)), VarExpr("y"), VarExpr("n"))
        assertEquals(VarExpr("n"), e.eval())

        e = If(Greater(Integer(6), Integer(3)), VarExpr("y"), VarExpr("n"))
        assertEquals(VarExpr("y"), e.eval())
    }

    @Test
    fun selectedBranchIsEvaluated() {
        // Guards the HoldRest implementation: the chosen branch is held on the way in but must still be evaluated on the way out
        assertEquals(Integer(2), If(BoolExpr.TRUE, Plus(Integer(1), Integer(1)), Integer(0)).eval())
        assertEquals(Integer(2), If(BoolExpr.FALSE, Integer(0), Plus(Integer(1), Integer(1))).eval())
    }

    @Test
    fun unselectedBranchIsNotEvaluated() {
        // Requires HoldRest: without it both branches run and x gets assigned
        val ctx = EvalContext()
        assertEquals(Integer(1), If(BoolExpr.TRUE, Integer(1), Set(VarExpr("x"), Integer(5))).eval(ctx))
        assertNull(ctx.lookup("x"))
    }

    @Test
    fun onlySelectedBranchHasSideEffects() {
        val ctx = EvalContext()
        If(BoolExpr.FALSE, Set(VarExpr("x"), Integer(1)), Set(VarExpr("x"), Integer(2))).eval(ctx)
        assertEquals(Integer(2), ctx.lookup("x"))
    }
}
