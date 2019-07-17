package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Test

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class ListExprTest {

    @Test
    fun equals() {

        val l1 = ListExpr(VarExpr("x"), IntegerNum(5), BoolExpr.FALSE)
        val l2 = ListExpr(VarExpr("x"), IntegerNum(6), BoolExpr.FALSE)
        val l3 = ListExpr(VarExpr("x"), IntegerNum(5), BoolExpr.FALSE)

        assertTrue(l1.equals(l3))
        assertFalse(l1.equals(l2))
    }

    @Test
    fun equals_nested() {
        var a = ListExpr(ListExpr(IntegerNum.ONE), ListExpr(VarExpr("a")))
        var b = ListExpr(ListExpr(IntegerNum.ONE), ListExpr(VarExpr("a")))
        assertTrue(a.equals(b))

        a = ListExpr(ListExpr(VarExpr("5"), IntegerNum(2)), ListExpr(VarExpr("a"), IntegerNum(1)), ListExpr(VarExpr("2"), IntegerNum(1)))
        b = ListExpr(ListExpr(VarExpr("5"), IntegerNum(2)), ListExpr(VarExpr("a"), IntegerNum(1)), ListExpr(VarExpr("2"), IntegerNum(1)))
        assertTrue(a.equals(b))
    }
}