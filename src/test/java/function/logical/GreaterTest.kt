package org.cerion.symcalc.function.logical

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import kotlin.test.Test
import kotlin.test.assertEquals

class GreaterTest {

    // All number ones should be better tested in NumberExpr tests with more generic comparison function

    @Test
    fun integers() {
        assertEquals(BoolExpr.TRUE, Greater(Integer(2), Integer(1)).eval())
        assertEquals(BoolExpr.FALSE, Greater(Integer(2), Integer(2)).eval())
        assertEquals(BoolExpr.TRUE, Greater(Integer(3), Integer(2), Integer(1)).eval())
        assertEquals(BoolExpr.FALSE, Greater(Integer(3), Integer(1), Integer(2)).eval())
        assertEquals(BoolExpr.FALSE, Greater(Integer(1), Integer(3), Integer(2)).eval())
    }

    @Test
    fun integer_withOthers() {
        assertEquals(BoolExpr.TRUE, Greater(Integer(5), Rational(9,2)).eval())
    }

    @Test
    fun simplification() {
        // First element
        // 2>1>x = 1 > x
        var e = Greater(Integer(2), Integer(1), VarExpr("x")).eval()
        assertEquals(Greater(Integer(1), VarExpr("x")), e)

        //Middle
        // x>5>3>1>y = x>5>1>y
        e = Greater(VarExpr("x"), Integer(5), Integer(3), Integer(1), VarExpr("y")).eval()
        assertEquals(Greater(VarExpr("x"), Integer(5), Integer(1), VarExpr("y")), e)

        // Last
        // x>2>1 = x>2
        e = Greater(VarExpr("x"), Integer(2), Integer(1)).eval()
        assertEquals(Greater(VarExpr("x"), Integer(2)), e)
    }
}