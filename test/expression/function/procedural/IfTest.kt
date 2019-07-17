package org.cerion.symcalc.expression.function.procedural

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.logical.Greater
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Test

import org.junit.Assert.*

class IfTest {

    @Test
    fun basic() {
        var e: Expr = If(Greater(IntegerNum(3), IntegerNum(6)), VarExpr("y"), VarExpr("n"))
        assertEquals(VarExpr("n"), e.eval())

        e = If(Greater(IntegerNum(6), IntegerNum(3)), VarExpr("y"), VarExpr("n"))
        assertEquals(VarExpr("y"), e.eval())
    }
}