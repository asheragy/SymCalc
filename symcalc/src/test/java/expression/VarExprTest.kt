package org.cerion.symcalc.expression

import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.number.Integer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class VarExprTest {
    @Test
    fun equals() {
        val v1 = VarExpr("x")
        val v2 = VarExpr("y")
        val v3 = VarExpr("x")

        assertNotEquals(v1, v2)
        assertNotEquals(v1 as Expr, Integer(5))
        assertEquals(v1, v3)
    }

    @Test
    fun eval() {
        val x = VarExpr("x")

        x.env.setVar("x", Integer(5))
        assertEquals(Integer(5), x.eval())

        x.env.setVar("x", Integer(2))
        assertEquals(Integer(2), x.eval())
    }

    @Test
    fun evalFunction() {
        val e = Plus(VarExpr("x"), VarExpr("x"))

        e.env.setVar("x", Integer(5))
        assertEquals(Integer(10), e.eval())

        e.env.setVar("x", Integer(3))
        assertEquals(Integer(6), e.eval())
    }

}