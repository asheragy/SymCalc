package org.cerion.symcalc.function.procedural

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.logical.Greater
import org.cerion.symcalc.expression.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals


class IfTest {

    @Test
    fun basic() {
        var e: Expr = If(Greater(Integer(3), Integer(6)), VarExpr("y"), VarExpr("n"))
        assertEquals(VarExpr("n"), e.eval())

        e = If(Greater(Integer(6), Integer(3)), VarExpr("y"), VarExpr("n"))
        assertEquals(VarExpr("y"), e.eval())
    }
}