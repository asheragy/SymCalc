package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.*
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec

class ArcTan(e: Expr) : TrigBase(e) {

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        if (x.isNegative)
            return Minus(ArcTan(x.unaryMinus())).eval() as RealBigDec
        else if (x.isZero)
            return RealBigDec.ZERO

        val dValue = x.toDouble()
        if (dValue > 0.7 && dValue < 3.0) {
            // Speed up convergence by using identity
            //   arctan(x) = 2*arctan( x / (1+ sqrt(1+x^2))

            val one = Integer.ONE
            val two = Integer.TWO
            return (two * ArcTan(x / (one + Sqrt(one + Power(x,two))))) as RealBigDec
        }
        else if(dValue < 0.71 ) {
            // TODO generic way to write taylor series since its used a lot
            // Basic taylor series, converges fast when < 0.7
            val xsquared = (x.unaryMinus() * x)
            var result = x
            var xpowi = x
            var denominator = Integer.ONE

            for(i in 0 until 100) {
                xpowi *= xsquared
                denominator += Integer.TWO
                val term = xpowi / denominator

                val t = (result + term) as RealBigDec
                if (result == t)
                    return result

                result = t
            }

            throw IterationLimitExceeded()
        }
        else {
            // ArcTan(x >= 3.0) is close to Pi/2 and normal taylor series converges slower
            var result = (Pi().eval(x.precision * 2) / Integer.TWO) as RealBigDec

            var xpowi = Integer.NEGATIVE_ONE / x
            val xsquared = xpowi.unaryMinus() * xpowi
            var denominator = Integer.NEGATIVE_ONE

            // TODO see if this can be rewritten to match the above one closer, loop starts a bit different
            for(i in 0 until 100) {
                denominator += Integer.TWO
                val term = xpowi / denominator

                val t = (result + term) as RealBigDec
                if (result == t)
                    return result

                result = t
                xpowi *= xsquared
            }

            throw IterationLimitExceeded()
        }
    }

    override fun evaluateAsDouble(d: Double): Double = kotlin.math.atan(d)

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                if (e.isOne)
                    return Divide(Pi(), Integer(4))
                else if (e.isZero)
                    return Integer.ZERO
            }
            is Infinity -> return Divide(Pi(), Integer(2))
            is ComplexInfinity -> return Indeterminate()
            is Power -> {
                if (e[0] == Integer(3)) {
                    if (e[1] == Rational(-1, 2))
                        return Divide(Pi(), Integer(6))
                    else if (e[1] == Rational.HALF)
                        return Divide(Pi(), Integer(3))
                }
            }
            is Times -> {
                if (e[0] == Rational.THIRD && e[1] == Power(Integer(3), Rational.HALF))
                    return Divide(Pi(), Integer(6))
            }
        }

        return this
    }

    /* This works but not when x > 10 or so plus not quite as fast as library version

    private fun custom(x: RealBigDec): Expr {
        val mc = MathContext(RealBigDec.getStoredPrecision(x.precision), RoundingMode.HALF_UP)

        var result = x.value.multiply(x.value, mc).plus(BigDecimal.ONE)
        result = x.value.divide(result, mc)

        var yOverX = result
        val y = result.multiply(x.value, mc) // x^2 / (1 + x^2)
        var factEven = BigDecimal(1.0)
        var factOdd = BigDecimal(1.0)

        for(i in 1 until 1000) {
            val n = i * 2
            factEven = factEven.multiply(BigDecimal(n))
            factOdd = factOdd.multiply(BigDecimal(n+1))

            yOverX = yOverX.multiply(y, mc)
            var e = factEven.divide(factOdd, mc)
            e = e.multiply(yOverX, mc)

            val t = result.add(e, mc)
            if (t == result)
                return RealBigDec(result, x.precision)

            result = t
        }

        return RealBigDec(result, x.precision)
    }
     */
}