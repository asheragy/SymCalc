package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.number.ComplexNum
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert
import org.junit.Test

class NumberQTest {

    @Test
    fun validation() {
        Assert.assertTrue(NumberQ().eval().isError)
        Assert.assertTrue(NumberQ(IntegerNum(5), IntegerNum(5)).eval().isError)
    }

    @Test
    fun basic() {
        Assert.assertEquals(BoolExpr.TRUE, NumberQ(IntegerNum(5)).eval())
        Assert.assertEquals(BoolExpr.TRUE, NumberQ(RealNum.create(2.34)).eval())
        Assert.assertEquals(BoolExpr.TRUE, NumberQ(Rational(2, 3)).eval())
        Assert.assertEquals(BoolExpr.TRUE, NumberQ(ComplexNum(5, 7)).eval())

        // Constant is not
        Assert.assertEquals(BoolExpr.FALSE, NumberQ(Pi()).eval())

        // Variable is not
        Assert.assertEquals(BoolExpr.FALSE, NumberQ(VarExpr("x")).eval())

        // Unless assigned to something that is NumberQ=true
        val e = CompoundExpression(
                Set(VarExpr("x"), IntegerNum(5)),
                NumberQ(VarExpr("x")))

        Assert.assertEquals(BoolExpr.TRUE, e.eval())
    }
}