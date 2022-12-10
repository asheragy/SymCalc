package org.cerion.symcalc.function.calculus

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.special.Gamma
import org.cerion.symcalc.function.special.Zeta
import org.cerion.symcalc.function.trig.*
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealDouble
import kotlin.test.Test

val x = VarExpr("x")

class DTest {
    private val Expr.dx: Expr
        get() = D(this, x)

    @Test
    fun constant() = assertAll(
        // Numbers
        Integer.TWO.dx `==` Integer.ZERO,
        RealDouble(2.354).dx `==` Integer.ZERO,
        Rational(4, 6).dx `==` Integer.ZERO,
        Complex.ZERO.dx `==` Integer.ZERO,

        // Constant
        Pi().dx`==` Integer.ZERO,

        // D(x,y) = 0
        VarExpr("y").dx `==` Integer.ZERO
    )

    @Test
    fun sumRule() {
        // D(1 + x) = 1
        (VarExpr("x") + VarExpr("y") + Integer(5)).dx `==` Integer.ONE
        // D(x - 5) = 1
        (VarExpr("x") - Integer(5)).dx `==` Integer.ONE

        // Sum rule
        // x + x = 2
        // x + sin(x) =
        // gamma(x) + zeta(x) = sum of derivatives
    }

    @Test
    fun productRule() {
        Times(5, x).dx `==` Integer(5)
        Times(5, Sin(x)).dx `==` Times(5, Cos(x))
        Times(x, Sin(x)).dx `==` Plus(Times(x, Cos(x)), Sin(x))

        // TODO bug with Times()
        //D(Times(Sin(x), Cos(x)), x) `==` Plus(Power(Cos(x), 2), Times(-1, Power(Sin(x), 2)))

        // Functions with no implemented derivative
        Times(Gamma(x), Sin(x)).dx `==` Gamma(x).dx * Sin(x) + Cos(x) * Gamma(x)
        Times(Gamma(x), Zeta(x)).dx `==` Gamma(x).dx * Zeta(x) + Zeta(x).dx * Gamma(x)
    }

    @Test
    fun quotientRule() {
        // sin / x
    }

    @Test
    fun power() {
        x.dx `==` Integer.ONE

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
        Sin(Times(5, x)).dx `==` Times(5, Cos(Times(5, x)))
        // sin(5x)
        // cos(5 + x)
        // sin(cos(x))
    }

    @Test
    fun trig() {
        Sin(x).dx `==` Cos(x)
        Cos(x).dx `==` Times(-1, Sin(x))
        Tan(x).dx `==` Power(Sec(x), 2)
        Sec(x).dx `==` Times(Sec(x), Tan(x))
        Csc(x).dx `==` Times(-1, Cot(x), Csc(x))
        Cot(x).dx `==` Times(-1, Power(Csc(x), 2))
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