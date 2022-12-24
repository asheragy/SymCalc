package org.cerion.symcalc.function.calculus

import org.cerion.symcalc.`==`
import org.cerion.symcalc.assertAll
import org.cerion.symcalc.constant.E
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.plus
import org.cerion.symcalc.expression.times
import org.cerion.symcalc.function.arithmetic.Log
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

private val x = VarExpr("x")
private val y = VarExpr("y")

class DTest {
    private val Expr.dx: Expr
        get() = D(this, x)

    @Test
    fun constant() = assertAll(
        // Numbers
        Integer.TWO.dx `==` 0,
        RealDouble(2.354).dx `==` 0,
        Rational(4, 6).dx `==` 0,
        Complex.ZERO.dx `==` 0,

        Pi().dx`==` 0,
        y.dx `==` 0
    )

    @Test
    fun sumRule() {
        (x + y + 5).dx `==` 1
        (x - 5).dx `==` 1
        (x + x).dx `==` 2
        (x + Sin(x)).dx `==` 1 + Cos(x)
        (Gamma(x) + Zeta(x)).dx `==` Gamma(x).dx + Zeta(x).dx
    }

    @Test
    fun productRule() {
        (5 * x).dx `==` 5
        (5 * Sin(x)).dx `==` 5 * Cos(x)
        (x * Sin(x)).dx `==` x * Cos(x) + Sin(x)

        D(Times(Sin(x), Cos(x)), x) `==` Plus(Power(Cos(x), 2), Times(-1, Power(Sin(x), 2)))

        // Functions with no implemented derivative
        (Gamma(x) * Sin(x)).dx `==` Gamma(x).dx * Sin(x) + Cos(x) * Gamma(x)
        (Gamma(x) * Zeta(x)).dx `==` Gamma(x).dx * Zeta(x) + Zeta(x).dx * Gamma(x)
    }

    @Test
    fun quotientRule() {
        // sin / x
    }

    @Test
    fun power() {
        x.dx `==` 1
        Power(x, 1).dx `==` 1
        Power(x, 2).dx `==` 2 * x
        Power(x, 3.14).dx `==` RealDouble(3.14) * Power(x, 2.14)
        Power(x, y).dx `==` Times(y, Power(x, y - 1))
    }

    @Test
    fun debug() {
        Power(x, Plus(y, -1)).eval()
        //Times(Power(x, y), Power(x, -1)).eval()
        //Power(x, y).dx.eval()
    }

    @Test
    fun exponential() {
        Power(99, x).dx `==` Power(99, x) * Log(99)
        Power(99, 5 * x).dx `==` 5 * Power(99, 5 * x) * Log(99)
        Power(E(), x).dx `==` Power(E(), x)
        Power(E(), 5 * x).dx `==` 5 * Power(E(), 5 * x)
        //Power(x, x).dx `==` Power(x, x) * (1 + Log(x))
    }

    @Test
    fun log() {
        // logA(x) = 1 / (ln(a) * x)
        // ln(x) = 1/x
    }

    @Test
    fun chainRule() {
        Sin(5 * x).dx `==` 5 * Cos(5 * x)
        Cos(5 + x).dx `==` -1 * Sin(5 + x)
        Sin(Cos(x)).dx `==` -1 * Cos(Cos(x)) * Sin(x)
    }

    @Test
    fun trig() {
        Sin(x).dx `==` Cos(x)
        Cos(x).dx `==` -1 * Sin(x)
        Tan(x).dx `==` Power(Sec(x), 2)
        Sec(x).dx `==` Sec(x) * Tan(x)
        Csc(x).dx `==` -1 * Cot(x) * Csc(x)
        Cot(x).dx `==` -1 * Power(Csc(x), 2)
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