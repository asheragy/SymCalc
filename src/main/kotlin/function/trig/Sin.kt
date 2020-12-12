package org.cerion.symcalc.function.trig

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.integer.Mod
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.sin

class Sin(e: Any) : TrigBase(e), StandardTrigFunction {

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }

    override fun evaluateAsDouble(d: Double): Double = sin(d)

    override fun evaluate(e: Expr): Expr {
        return this
    }

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        // Pi * Integer = 0
        if (e is Integer)
            return Integer.ZERO

        if (e is Rational) {
            val ratio =
            when(e.denominator) {
                Integer((2)) -> Integer.ONE
                Integer(3) -> sqrt3Over2
                Integer(4) -> oneOverSqrt2
                Integer(6) -> Rational(1,2)
                else -> return this
            }

            val mod = e.numerator % (Integer.TWO * e.denominator)
            if (mod > e.denominator)
                return Minus(ratio).eval()

            return ratio
        }

        return this // Could not evaluate
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        val mc = MathContext(RealBigDec.getStoredPrecision(x.precision), RoundingMode.HALF_UP)

        // Normalize to range of 0 to 2pi
        val x2pi =
                if (x.value.toDouble() >= 0 && x.value.toDouble() < (2*Math.PI))
                    x
                else
                    Mod(x, Integer(2) * Pi()).eval() as RealBigDec

        var result = x2pi.value
        var factorial = BigDecimal(1.0)
        val xsquared = x2pi.value.pow(2)
        var power = x2pi.value

        // Taylor series x - x^3/3! + x^5/5! ...
        for(i in 1..1000) {
            val n = (i * 2) + 1
            power = power.multiply(xsquared, mc)                 // x^n
            factorial = factorial.times(BigDecimal(n * (n-1)))   // n!
            val e = power.divide(factorial, mc)

            val t = if (i % 2 == 0)
                        result.add(e, mc)
                    else
                        result.subtract(e, mc)

            if (t == result)
                return RealBigDec(result, x.precision)

            result = t
        }

        throw IterationLimitExceeded()
    }
}
