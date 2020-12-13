package org.cerion.symcalc.function.special

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.integer.Bernoulli
import org.cerion.symcalc.function.integer.Factorial
import org.nevec.rjm.BigDecimalMath
import java.math.MathContext

class Zeta(vararg e: Expr) : FunctionExpr(*e) {
    override fun evaluate(): Expr {
        when (val e = get(0)) {
            is Integer -> {
                if (e.isZero)
                    return Rational.HALF.unaryMinus()
                if (e.isOne)
                    return ComplexInfinity()

                if (e.isEven) {
                    if (e.isNegative)
                        return Integer.ZERO

                    // Positive even integers is Pi^n * 2^(n - 1)* Abs[BernoulliB[n]]/n!
                    val bernoulli = Bernoulli(e).eval() as Rational
                    return Power(Pi(),e) * Power(Integer(2), e - 1) * bernoulli.abs() / Factorial(e)
                }
                else if (e.isNegative) {
                    // Negative odd integers are -BernoulliB[1 - n]/(1 - n)
                    return Integer.NEGATIVE_ONE * Bernoulli(Integer.ONE - e) / (Integer.ONE - e)
                }

            }

            is RealDouble -> {
                if (e.isNegative && e.isWholeNumber() && e.value.toInt() % 2 == 0)
                    return RealDouble(0.0)
            }

            is RealBigDec -> {
                if (e.isWholeNumber()) {
                    val n = e.toInteger()
                    if (n.isNegative && n.isEven)
                        return RealBigDec.ZERO

                    return RealBigDec(BigDecimalMath.zeta(n.intValue(), MathContext(e.value.precision())), e.precision)
                }

                return calculateInfiniteSum(e)
            }
        }

        return this
    }

    private fun calculateInfiniteSum(s: RealBigDec): RealBigDec {
        var sum: Expr = Integer.ONE

        for(n in 2 until 100) {
            val term = Integer.ONE / Power(Integer(n), s)
            sum += term
        }

        return sum as RealBigDec
    }
}