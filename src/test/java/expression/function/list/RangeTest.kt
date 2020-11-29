package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RangeTest {

    @Test
    fun singleParameter() {
        assertEquals(ListExpr(1), Range(Integer(1)).eval())
        assertEquals(ListExpr(1,2,3,4,5), Range(Integer(5)).eval())
        assertEquals(ListExpr(1,2,3), Range(RealDouble(3.4)).eval())
    }

    @Test
    fun minMax() {
        assertEquals(ListExpr(5,6,7,8,9,10), Range(Integer(5), Integer(10)).eval())
        assertEquals(ListExpr(-4,-3,-2,-1,0), Range(Integer(-4), Integer(0)).eval())
    }

    @Test
    fun step() {
        assertEquals(ListExpr(0,2,4,6,8,10), Range(Integer(0), Integer(10), Integer(2)).eval())
        assertEquals(ListExpr(1,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0), Range(Integer(1), Integer(5), RealDouble(0.5)).eval())
    }

    @Test
    fun noValues() {
        assertEquals(ListExpr(), Range(Integer(-4)).eval())
        assertEquals(ListExpr(), Range(Integer(0)).eval())
        assertEquals(ListExpr(), Range(Integer(4), Integer(-4)).eval())
    }

}