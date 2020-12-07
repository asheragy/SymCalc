package org.cerion.symcalc.function.list

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals


class VectorQTest {

    @Test
    fun isVector() {
        assertEquals(BoolExpr.TRUE, VectorQ(ListExpr()).eval())
        assertEquals(BoolExpr.TRUE, VectorQ(ListExpr(Integer(3))).eval())
        assertEquals(BoolExpr.TRUE, VectorQ(ListExpr(Integer(3), RealDouble(2.35))).eval())
    }

    @Test
    fun isNotVector() {
        // Not a list
        assertEquals(BoolExpr.FALSE, VectorQ(Integer(5)).eval())

        // More than 1 dimension
        assertEquals(BoolExpr.FALSE, VectorQ(ListExpr(Integer(5), ListExpr())).eval())
    }
}