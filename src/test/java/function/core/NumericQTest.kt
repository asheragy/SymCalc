package org.cerion.symcalc.function.core

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals


class NumericQTest {

    @Test
    fun basic() {
        // Number
        assertEquals(BoolExpr.TRUE, NumericQ(Integer(5)).eval())
        assertEquals(BoolExpr.TRUE, NumericQ(RealDouble(4.576)).eval())

        // Constant
        assertEquals(BoolExpr.TRUE, NumericQ(Pi()).eval())

        // NumericFunction AND all parameters are NumbericQ=true
        assertEquals(BoolExpr.TRUE, NumericQ(Plus(Integer(2), Integer(3))).eval())
        assertEquals(BoolExpr.TRUE, NumericQ(Plus(Integer(2), Pi())).eval())

        // Variable assigned to number
        val e = CompoundExpression(
                Set(VarExpr("x"), Integer(5)),
                NumericQ(VarExpr("x")))

        assertEquals(BoolExpr.TRUE, e.eval())
    }

    @Test
    fun nonNumeric() {
        // Numeric inside non-numberic
        assertEquals(BoolExpr.FALSE, NumericQ(ListExpr(Integer(5))).eval())

        // Non-numeric inside numericFunction
        assertEquals(BoolExpr.FALSE, NumericQ(Plus(Integer(5), ListExpr())).eval())

        // Variable
        assertEquals(BoolExpr.FALSE, NumericQ(VarExpr("x")).eval())
    }
}