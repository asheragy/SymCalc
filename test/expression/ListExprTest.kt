package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.number.Integer
import org.junit.Test

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class ListExprTest {

    @Test
    fun equals() {

        val l1 = ListExpr(VarExpr("x"), Integer(5), BoolExpr.FALSE)
        val l2 = ListExpr(VarExpr("x"), Integer(6), BoolExpr.FALSE)
        val l3 = ListExpr(VarExpr("x"), Integer(5), BoolExpr.FALSE)

        assertTrue(l1.equals(l3))
        assertFalse(l1.equals(l2))
    }

    @Test
    fun equals_nested() {
        var a = ListExpr(ListExpr(Integer.ONE), ListExpr(VarExpr("a")))
        var b = ListExpr(ListExpr(Integer.ONE), ListExpr(VarExpr("a")))
        assertTrue(a.equals(b))

        a = ListExpr(ListExpr(VarExpr("5"), Integer(2)), ListExpr(VarExpr("a"), Integer(1)), ListExpr(VarExpr("2"), Integer(1)))
        b = ListExpr(ListExpr(VarExpr("5"), Integer(2)), ListExpr(VarExpr("a"), Integer(1)), ListExpr(VarExpr("2"), Integer(1)))
        assertTrue(a.equals(b))
    }
}