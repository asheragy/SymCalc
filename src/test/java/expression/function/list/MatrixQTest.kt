package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MatrixQTest {

    @Test
    fun isMatrix() {
        val a = Integer(3)
        val b = RealDouble(6.32)
        val c = VarExpr("x")

        // 0x0
        assertEquals(BoolExpr.TRUE, MatrixQ(ListExpr(ListExpr())).eval())

        // 1x1
        assertEquals(BoolExpr.TRUE, MatrixQ(ListExpr(ListExpr(a))).eval())

        // 2x3
        assertEquals(BoolExpr.TRUE, MatrixQ(ListExpr(
                ListExpr(a, b, c),
                ListExpr(c, b, a)
        )).eval())
    }

    @Test
    fun isNotMatrix() {
        val a = Integer(3)
        val b = RealDouble(6.32)
        val c = VarExpr("x")

        // Not a list
        assertEquals(BoolExpr.FALSE, MatrixQ(a).eval())

        // Not 2 dimension
        assertEquals(BoolExpr.FALSE, MatrixQ(ListExpr(a, b)).eval())

        // More than 2 dimension
        assertEquals(BoolExpr.FALSE, MatrixQ(ListExpr(
                ListExpr(a, b, c),
                ListExpr(c, b, ListExpr(c))
        )).eval())

        // Size does not match
        assertEquals(BoolExpr.FALSE, MatrixQ(ListExpr(
                ListExpr(a, b, c),
                ListExpr(c, b)
        )).eval())
    }
}