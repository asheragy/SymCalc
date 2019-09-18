package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.function.integer.Bernoulli
import org.cerion.symcalc.expression.function.integer.Mod
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.tan

class Tan(vararg e: Expr) : TrigBase(Function.TAN, *e), StandardTrigFunction {

    override fun evaluateAsDouble(d: Double): Double = tan(d)

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        // Pi * Integer = 0
        if (e is Integer)
            return Integer.ZERO

        if (e is Rational) {
            val ratio =
                    when(e.denominator) {
                        Integer((2)) -> ComplexInfinity()
                        Integer(3) -> sqrt3
                        Integer(4) -> Integer.ONE
                        Integer(6) -> oneOverSqrt3
                        else -> return this
                    }

            val mod = e.numerator % (Integer.TWO * e.denominator)
            val position = mod.intValue().toDouble() / (e.denominator.intValue() * 2)
            if (position in 0.250001..0.50 || position in 0.750001..1.0)
                return Minus(ratio).eval()

            return ratio
        }

        return this // Could not evaluate
    }

    override fun evaluate(e: Expr): Expr {
        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        val mc = MathContext(x.precision+5, RoundingMode.HALF_UP)

        // TODO may need to be Pi/2
        // Normalize to range of 0 to Pi
        var xmodpi =
                if (x.value.toDouble() >= 0 && x.value.toDouble() < (Math.PI / 2))
                    x
                else
                    Mod(x, Divide(Pi(), Integer(2))).eval() as RealBigDec

        val xmodpiCheck = Mod(x, Pi()).eval() as RealBigDec
        val negate = xmodpiCheck.value > xmodpi.value
        if (negate)
            xmodpi = Subtract(Divide(Pi(), Integer(2)), xmodpi).eval() as RealBigDec

        var result = xmodpi.value
        var factorial = BigDecimal(2.0)

        // Taylor series x - x^3/3! + x^5/5! ...
        for(n in 2..200) {
            val n2 = n * 2
            val bernoulli = Bernoulli(Integer(n2)).eval(x.precision + 5) as RealBigDec
            val neg4pow = BigDecimal(-4.0).pow(n, mc)
            val one4pow = BigDecimal(1.0).subtract(BigDecimal(4.0).pow(n, mc), mc)
            factorial = factorial.multiply(BigDecimal(n2-1))
            factorial = factorial.multiply(BigDecimal(n2))
            val xpow = xmodpi.value.pow(n2 - 1)

            var term = bernoulli.value.multiply(neg4pow, mc)
            term = term.multiply(one4pow, mc)
            term = term.multiply(xpow, mc)
            term = term.divide(factorial, mc)

            val t = result.add(term, mc)

            if (t == result) {
                val ret = RealBigDec(result.round(MathContext(x.precision, RoundingMode.HALF_UP)))
                if (negate)
                    return ret.unaryMinus()

                return ret
            }

            result = t
        }

        //return RealBigDec(result.round(MathContext(x.precision, RoundingMode.HALF_UP)))
        throw IterationLimitExceeded()
    }

    companion object {
        val sqrt3 = Power(Integer(3), Rational(1,2))
        val oneOverSqrt3 = Power(Integer(3), Rational(-1,2))
    }
}