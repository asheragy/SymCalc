package org.cerion.symcalc.expression.function.logical

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.junit.Assert.assertEquals
import org.junit.Test

class GreaterTest {

    @Test
    fun basic() {
        assertEquals(BoolExpr.TRUE, Greater(IntegerNum(2), IntegerNum(1)).eval())
        assertEquals(BoolExpr.FALSE, Greater(IntegerNum(2), IntegerNum(2)).eval())
        assertEquals(BoolExpr.TRUE, Greater(IntegerNum(3), IntegerNum(2), IntegerNum(1)).eval())
        assertEquals(BoolExpr.FALSE, Greater(IntegerNum(3), IntegerNum(1), IntegerNum(2)).eval())
        assertEquals(BoolExpr.FALSE, Greater(IntegerNum(1), IntegerNum(3), IntegerNum(2)).eval())
    }

    @Test
    fun simplification() {
        // First element
        // 2>1>x = 1 > x
        var e = Greater(IntegerNum(2), IntegerNum(1), VarExpr("x")).eval()
        assertEquals(Greater(IntegerNum(1), VarExpr("x")), e)

        //Middle
        // x>5>3>1>y = x>5>1>y
        e = Greater(VarExpr("x"), IntegerNum(5), IntegerNum(3), IntegerNum(1), VarExpr("y")).eval()
        assertEquals(Greater(VarExpr("x"), IntegerNum(5), IntegerNum(1), VarExpr("y")), e)

        // Last
        // x>2>1 = x>2
        e = Greater(VarExpr("x"), IntegerNum(2), IntegerNum(1)).eval()
        assertEquals(Greater(VarExpr("x"), IntegerNum(2)), e)
    }
}