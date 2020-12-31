package org.cerion.symcalc.function.special

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.integer.Bernoulli
import org.cerion.symcalc.function.integer.Binomial
import org.cerion.symcalc.function.integer.Factorial
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble
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

                    if (n.isEven)
                        return Zeta(n).eval(e.precision)

                    return RealBigDec(BigDecimalMath.zeta(n.intValue(), MathContext(e.value.precision())), e.precision)
                }

                return calculatePSeries(e)
                //return calculateDirichletEta(e)
            }
        }

        return this
    }

    private fun calculateDirichletEta(s: RealBigDec): RealBigDec {
        var sum = RealBigDec("0.5", s.precision)
        val one = RealBigDec("1.0", s.precision)

        var inv2pow = RealBigDec("0.5", s.precision)
        for (n in 1 until 100) {
            val binomial = Binomial(n).eval() as ListExpr
            var innersum = one
            for (k in 1..n) {
                val term = (binomial[k] * Power(k+1, s.unaryMinus())) as RealBigDec
                if (k % 2 == 0)
                    innersum = (innersum + term) as RealBigDec
                else
                    innersum = (innersum - term) as RealBigDec
            }

            inv2pow /= RealBigDec("2.0", s.precision)
            val t = sum + (inv2pow * innersum)
            if (sum == t)
                break

            sum = t as RealBigDec
        }

        val multiplier = one / (one - Power(2, one - s))
        return (multiplier * sum) as RealBigDec
    }

    // Converges faster for larger s
    private fun calculatePSeries(s: RealBigDec): RealBigDec {
        var sum = RealBigDec("1.0", s.precision)

        for(n in 2..100) {
            val term = Integer.ONE / Power(Integer(n), s)
            sum = (sum + term) as RealBigDec
        }

        return sum
    }
}