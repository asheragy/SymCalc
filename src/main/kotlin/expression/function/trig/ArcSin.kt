package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Sqrt
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.asin

class ArcSin(e: Any) : TrigBase(e) {

    // FEAT Complex and more trig tables

    override fun evaluateAsDouble(d: Double): Double = asin(d)

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                when (e) {
                    Integer.ONE -> return Divide(Pi(), 2).eval()
                    Integer.NEGATIVE_ONE -> return Divide(Pi(), -2).eval()
                    Integer.ZERO -> return Integer.ZERO
                }
            }
        }

        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        if (x.isZero)
            return x
        if (x.isNegative)
            return Minus(ArcSin(x.unaryMinus())).eval()
        if (x > RealDouble(0.7))
            return Divide(Pi(), 2) - ArcSin(Sqrt(Integer(1) - x.square()))

        val mc = MathContext(RealBigDec.getStoredPrecision(x.precision), RoundingMode.HALF_UP)

        var result = x.value
        val xsquared = x.value.pow(2)
        var xpow = x.value
        var numerator = BigDecimal(1.0)
        var denominator = BigDecimal(1.0)

        // Taylor series x + x^3/6 + x^5/40 ...
        for(n in 1..1000) {
            xpow = xpow.multiply(xsquared, mc)
            val n2 = (n * 2) - 1
            numerator = numerator.multiply(n2.toBigDecimal())

            val d1 = (n * 2)
            denominator = denominator.divide((d1 - 1).toBigDecimal())
            denominator = denominator.multiply((d1 * (d1+1)).toBigDecimal())

            var e = xpow.multiply(numerator, mc)
            e = e.divide(denominator, mc)

            val t = result.add(e, mc)
            if (t == result)
                return RealBigDec(result, x.precision)

            result = t
        }

        throw IterationLimitExceeded()
    }
}