package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.integer.Mod
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.cos

class Cos(e: Expr) : TrigBase(e), StandardTrigFunction {

    override fun evaluateAsDouble(d: Double): Double = cos(d)

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        if (e is Integer) {
            if (e.isOdd)
                return Integer.NEGATIVE_ONE
            return Integer.ONE
        }

        if (e is Rational) {
            var offset = Integer.ZERO

            val ratio =
            when(e.denominator) {
                Integer((2)) -> Integer.ZERO
                Integer(3) -> {
                    offset = Integer.ONE
                    Rational.HALF
                }
                Integer(4) -> {
                    offset = Integer.TWO
                    oneOverSqrt2
                }
                Integer(6) -> {
                    offset = Integer(3)
                    sqrt3Over2
                }
                else -> return this
            }

            val mod = (e.numerator + offset) % (Integer.TWO * e.denominator)
            if (mod >= e.denominator)
                return Minus(ratio).eval()

            return ratio
        }

        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        val mc = MathContext( RealBigDec.getStoredPrecision(x.precision), RoundingMode.HALF_UP)

        // Normalize to range of 0 to 2pi
        val x2pi =
                if (x.value.toDouble() >= 0 && x.value.toDouble() < (2*Math.PI))
                    x
                else
                    Mod(x, Integer(2) * Pi()).eval() as RealBigDec

        var result = BigDecimal(1.0)
        var factorial = BigDecimal(1.0)
        val xsquared = x2pi.value.pow(2)
        var power = BigDecimal(1.0)

        // Taylor series 1 - x^2/2! + x^4/4! ...
        for(i in 1..1000) {
            val n = i * 2
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

    override fun evaluate(e: Expr): Expr {
        return this
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }
}
