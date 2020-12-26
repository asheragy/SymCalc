package org.cerion.symcalc.expression

import org.cerion.symcalc.`should equal`
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class ListExprTest {

    @Test
    fun toStringTest() {
        assertEquals("{5, 3.14, Pi}", ListExpr(Integer(5), RealDouble(3.14), Pi()).toString())
    }

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
        b = ListExpr(ListExpr(VarExpr("5"), 2), ListExpr(VarExpr("a"), Integer(1)), ListExpr(VarExpr("2"), Integer(1)))
        assertTrue(a.equals(b))
    }

    @Test
    fun nonExprParameters() {
        ListExpr(5, 1.23, "x", "12345", "3.14") `should equal` ListExpr(Integer(5), RealDouble(1.23), VarExpr("x"), Integer("12345"), RealBigDec("3.14"))
    }
}