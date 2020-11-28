package org.cerion.symcalc.expression.function.calculus

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.trig.Cos
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.Complex
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.test.Test
import kotlin.test.assertEquals

class DTest {

    @Test
    fun single() {
        // Numbers
        assertEquals(Integer.ZERO, D(Integer.TWO, VarExpr("x")).eval())
        assertEquals(Integer.ZERO, D(RealDouble(2.354), VarExpr("x")).eval())
        assertEquals(Integer.ZERO, D(Rational(4, 6), VarExpr("x")).eval())
        assertEquals(Integer.ZERO, D(Complex.ZERO, VarExpr("x")).eval())

        // Constant
        assertEquals(Integer.ZERO, D(Pi(), VarExpr("x")).eval())

        // D(x,x) = 1
        assertEquals(Integer.ONE, D(VarExpr("x"), VarExpr("x")).eval())

        // D(x,y) = 0
        assertEquals(Integer.ZERO, D(VarExpr("y"), VarExpr("x")).eval())
    }

    @Test
    fun basic() {
        // D(1 + x) = 1
        assertEquals(Integer.ONE, D(VarExpr("x") + VarExpr("y") + Integer(5), VarExpr("x")).eval())

        // D(x - 5) = 1
        assertEquals(Integer.ONE, D(VarExpr("x") - Integer(5), VarExpr("x")).eval())
    }

    @Test
    fun trig() {
        // D(Sin(x)) = Cos(x)
        var expected: Expr = Cos(VarExpr("x"))
        var actual = D(Sin(VarExpr("x")), VarExpr("x")).eval()
        assertEquals(expected, actual)

        // D(Cos(x)) = -Sin(x)
        expected = Times(Integer(-1), Sin(VarExpr("x")))
        actual = D(Cos(VarExpr("x")), VarExpr("x")).eval()
        assertEquals(expected, actual)
    }
}