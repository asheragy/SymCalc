package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealDouble
import org.junit.Assert
import org.junit.Test


class VectorQTest {

    @Test
    fun isVector() {
        Assert.assertEquals(BoolExpr.TRUE, VectorQ(ListExpr()).eval())
        Assert.assertEquals(BoolExpr.TRUE, VectorQ(ListExpr(Integer(3))).eval())
        Assert.assertEquals(BoolExpr.TRUE, VectorQ(ListExpr(Integer(3), RealDouble(2.35))).eval())
    }

    @Test
    fun isNotVector() {
        // Not a list
        Assert.assertEquals(BoolExpr.FALSE, VectorQ(Integer(5)).eval())

        // More than 1 dimension
        Assert.assertEquals(BoolExpr.FALSE, VectorQ(ListExpr(Integer(5), ListExpr())).eval())
    }
}