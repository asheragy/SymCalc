package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert
import org.junit.Test


class NumericQTest {

    @Test
    fun basic() {
        // Number
        Assert.assertEquals(BoolExpr.TRUE, NumericQ(IntegerNum(5)).eval())
        Assert.assertEquals(BoolExpr.TRUE, NumericQ(RealNum.create(4.576)).eval())

        // Constant
        Assert.assertEquals(BoolExpr.TRUE, NumericQ(Pi()).eval())

        // NumericFunction AND all parameters are NumbericQ=true
        Assert.assertEquals(BoolExpr.TRUE, NumericQ(Plus(IntegerNum(2), IntegerNum(3))).eval())
        Assert.assertEquals(BoolExpr.TRUE, NumericQ(Plus(IntegerNum(2), Pi())).eval())

        // Variable assigned to number
        val e = CompoundExpression(
                Set(VarExpr("x"), IntegerNum(5)),
                NumericQ(VarExpr("x")))

        Assert.assertEquals(BoolExpr.TRUE, e.eval())
    }

    @Test
    fun nonNumeric() {
        // Numeric inside non-numberic
        Assert.assertEquals(BoolExpr.FALSE, NumericQ(ListExpr(IntegerNum(5))).eval())

        // Non-numeric inside numericFunction
        Assert.assertEquals(BoolExpr.FALSE, NumericQ(Plus(IntegerNum(5), ListExpr())).eval())

        // Variable
        Assert.assertEquals(BoolExpr.FALSE, NumericQ(VarExpr("x")).eval())
    }
}