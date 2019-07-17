package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert
import org.junit.Test


class VectorQTest {

    @Test
    fun isVector() {
        Assert.assertEquals(BoolExpr.TRUE, VectorQ(ListExpr()).eval())
        Assert.assertEquals(BoolExpr.TRUE, VectorQ(ListExpr(IntegerNum(3))).eval())
        Assert.assertEquals(BoolExpr.TRUE, VectorQ(ListExpr(IntegerNum(3), RealNum.create(2.35))).eval())
    }

    @Test
    fun isNotVector() {
        // Not a list
        Assert.assertEquals(BoolExpr.FALSE, VectorQ(IntegerNum(5)).eval())

        // More than 1 dimension
        Assert.assertEquals(BoolExpr.FALSE, VectorQ(ListExpr(IntegerNum(5), ListExpr())).eval())
    }
}