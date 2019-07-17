package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert
import org.junit.Test

class MatrixQTest {

    @Test
    fun isMatrix() {
        val a = IntegerNum(3)
        val b = RealNum.create(6.32)
        val c = VarExpr("x")

        // 0x0
        Assert.assertEquals(BoolExpr.TRUE, MatrixQ(ListExpr(ListExpr())).eval())

        // 1x1
        Assert.assertEquals(BoolExpr.TRUE, MatrixQ(ListExpr(ListExpr(a))).eval())

        // 2x3
        Assert.assertEquals(BoolExpr.TRUE, MatrixQ(ListExpr(
                ListExpr(a, b, c),
                ListExpr(c, b, a)
        )).eval())
    }

    @Test
    fun isNotMatrix() {
        val a = IntegerNum(3)
        val b = RealNum.create(6.32)
        val c = VarExpr("x")

        // Not a list
        Assert.assertEquals(BoolExpr.FALSE, MatrixQ(a).eval())

        // Not 2 dimension
        Assert.assertEquals(BoolExpr.FALSE, MatrixQ(ListExpr(a, b)).eval())

        // More than 2 dimension
        Assert.assertEquals(BoolExpr.FALSE, MatrixQ(ListExpr(
                ListExpr(a, b, c),
                ListExpr(c, b, ListExpr(c))
        )).eval())

        // Size does not match
        Assert.assertEquals(BoolExpr.FALSE, MatrixQ(ListExpr(
                ListExpr(a, b, c),
                ListExpr(c, b)
        )).eval())
    }
}