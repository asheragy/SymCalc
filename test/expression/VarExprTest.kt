package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals


class VarExprTest {
    @Test
    fun equals() {
        val v1 = VarExpr("x")
        val v2 = VarExpr("y")
        val v3 = VarExpr("x")

        assertNotEquals(v1, v2)
        assertNotEquals(v1, IntegerNum(5))
        assertEquals(v1, v3)
    }

    @Test
    fun eval() {
        val x = VarExpr("x")

        x.env.setVar("x", IntegerNum(5))
        assertEquals(IntegerNum(5), x.eval())

        x.env.setVar("x", IntegerNum(2))
        assertEquals(IntegerNum(2), x.eval())
    }

    @Test
    fun evalFunction() {
        val e = Plus(VarExpr("x"), VarExpr("x"))

        e.env.setVar("x", IntegerNum(5))
        assertEquals(IntegerNum(10), e.eval())

        e.env.setVar("x", IntegerNum(3))
        assertEquals(IntegerNum(6), e.eval())
    }

}