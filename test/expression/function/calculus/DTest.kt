package org.cerion.symcalc.expression.function.calculus

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.trig.*
import org.cerion.symcalc.expression.number.*
import org.junit.Assert
import org.junit.Test

class DTest {

    @Test
    fun single() {
        // Numbers
        Assert.assertEquals(Integer.ZERO, D(Integer.TWO, VarExpr("x")).eval())
        Assert.assertEquals(Integer.ZERO, D(RealDouble(2.354), VarExpr("x")).eval())
        Assert.assertEquals(Integer.ZERO, D(Rational(4, 6), VarExpr("x")).eval())
        Assert.assertEquals(Integer.ZERO, D(Complex.ZERO, VarExpr("x")).eval())

        // Constant
        Assert.assertEquals(Integer.ZERO, D(Pi(), VarExpr("x")).eval())

        // D(x,x) = 1
        Assert.assertEquals(Integer.ONE, D(VarExpr("x"), VarExpr("x")).eval())

        // D(x,y) = 0
        Assert.assertEquals(Integer.ZERO, D(VarExpr("y"), VarExpr("x")).eval())
    }

    @Test
    fun basic() {
        // D(1 + x) = 1
        Assert.assertEquals(Integer.ONE,
                D(Plus(VarExpr("x"), VarExpr("y"), Integer(5)), VarExpr("x")).eval())

        // D(x - 5) = 1
        Assert.assertEquals(Integer.ONE,
                D(Subtract(VarExpr("x"), Integer(5)), VarExpr("x")).eval())
    }

    @Test
    fun trig() {
        // D(Sin(x)) = Cos(x)
        var expected: Expr = Cos(VarExpr("x"))
        var actual = D(Sin(VarExpr("x")), VarExpr("x")).eval()
        Assert.assertEquals(expected, actual)

        // D(Cos(x)) = -Sin(x)
        expected = Times(Integer(-1), Sin(VarExpr("x")))
        actual = D(Cos(VarExpr("x")), VarExpr("x")).eval()
        Assert.assertEquals(expected, actual)
    }
}