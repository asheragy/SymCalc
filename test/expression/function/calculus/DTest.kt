package org.cerion.symcalc.expression.function.calculus

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.trig.*
import org.cerion.symcalc.expression.number.ComplexNum
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealNum
import org.junit.Assert
import org.junit.Test


class DTest {

    @Test
    fun single() {
        // Numbers
        Assert.assertEquals(IntegerNum.ZERO, D(IntegerNum.TWO, VarExpr("x")).eval())
        Assert.assertEquals(IntegerNum.ZERO, D(RealNum.create(2.354), VarExpr("x")).eval())
        Assert.assertEquals(IntegerNum.ZERO, D(Rational(4, 6), VarExpr("x")).eval())
        Assert.assertEquals(IntegerNum.ZERO, D(ComplexNum.ZERO, VarExpr("x")).eval())

        // Constant
        Assert.assertEquals(IntegerNum.ZERO, D(Pi(), VarExpr("x")).eval())

        // D(x,x) = 1
        Assert.assertEquals(IntegerNum.ONE, D(VarExpr("x"), VarExpr("x")).eval())

        // D(x,y) = 0
        Assert.assertEquals(IntegerNum.ZERO, D(VarExpr("y"), VarExpr("x")).eval())
    }

    @Test
    fun basic() {
        // D(1 + x) = 1
        Assert.assertEquals(IntegerNum.ONE,
                D(Plus(VarExpr("x"), VarExpr("y"), IntegerNum(5)), VarExpr("x")).eval())

        // D(x - 5) = 1
        Assert.assertEquals(IntegerNum.ONE,
                D(Subtract(VarExpr("x"), IntegerNum(5)), VarExpr("x")).eval())
    }

    @Test
    fun trig() {
        // D(Sin(x)) = Cos(x)
        var expected: Expr = Cos(VarExpr("x"))
        var actual = D(Sin(VarExpr("x")), VarExpr("x")).eval()
        Assert.assertEquals(expected, actual)

        // D(Cos(x)) = -Sin(x)
        expected = Times(IntegerNum(-1), Sin(VarExpr("x")))
        actual = D(Cos(VarExpr("x")), VarExpr("x")).eval()
        Assert.assertEquals(expected, actual)
    }
}