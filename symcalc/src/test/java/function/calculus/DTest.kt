package org.cerion.symcalc.function.calculus

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.trig.Cos
import org.cerion.symcalc.function.trig.Sin
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealDouble
import org.cerion.symcalc.`should equal`
import kotlin.test.Test

class DTest {

    @Test
    fun constant() = assertAll(
        // Numbers
        D(Integer.TWO, VarExpr("x")) `==` Integer.ZERO,
        D(RealDouble(2.354), VarExpr("x")) `==` Integer.ZERO,
        D(Rational(4, 6), VarExpr("x")) `==` Integer.ZERO,
        D(Complex.ZERO, VarExpr("x")) `==` Integer.ZERO,

        // Constant
        D(Pi(), VarExpr("x")).eval() `==` Integer.ZERO,

        // D(x,y) = 0
        D(VarExpr("y"), VarExpr("x")) `==` Integer.ZERO
    )

    @Test
    fun sumRule() {
        // D(1 + x) = 1
        D(VarExpr("x") + VarExpr("y") + Integer(5), VarExpr("x")).eval() `should equal` Integer.ONE
        // D(x - 5) = 1
        D(VarExpr("x") - Integer(5), VarExpr("x")).eval() `should equal` Integer.ONE

        // Sum rule
        // x + x = 2
        // x + sin(x) =
        // gamma(x) + zeta(x) = sum of derivatives
    }

    @Test
    fun productRule() {
        // sin*cos
        // 5 * sin
        // gamma * sin
        // gamma * zeta
    }

    @Test
    fun quotientRule() {
        // sin / x
    }

    @Test
    fun power() {
        D(VarExpr("x"), VarExpr("x")).eval() `should equal` Integer.ONE

        //D(Power(VarExpr("x"), Integer.TWO), VarExpr("x")).eval() `should equal` Integer.ONE

        // x^n = n*x^(n-1)
        // Check misc things with and without x in the exponent

        // a^x = a^x * ln(a)
        // e^x = e^x
        // e^5x = ??
    }

    @Test
    fun log() {
        // logA(x) = 1 / (ln(a) * x)
        // ln(x) = 1/x
    }

    @Test
    fun chainRule() {
        // sin(5x)
        // sin(cos(x))
    }

    @Test
    fun trig() {
        D(Sin(VarExpr("x")), VarExpr("x")).eval() `should equal` Cos(VarExpr("x"))
        D(Cos(VarExpr("x")), VarExpr("x")).eval() `should equal` Times(Integer(-1), Sin(VarExpr("x")))
        // Tan(x) = sec^2(x)
        // cot(x) = -cosec^2(x)
        // sec(x) = sec(x)*tan(x)
        // cosec(x) = -cosec(x)*cot(x)

        // sin(5x) and sin(5 + x)
    }

    @Test
    fun inverseTrig() {
        // all 6 functions
    }

    @Test
    fun hyperbolicTrig() {
        // sinh(x) = cosh(x)
        // cosh(x) = sinh(x)
        // tanh(x) = sech^2(x)
        // coth(x) = -cosech^2(x)
        // sech(x) = -sech(x)*tanh(x)
        // coseh(x) = -cosech(x)*coth(x)
    }
}