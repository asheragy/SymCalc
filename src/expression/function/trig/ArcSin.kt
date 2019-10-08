package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealBigDec
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class ArcSin(e: Any) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluate(e: Expr): Expr {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        val mc = MathContext(RealBigDec.getStoredPrecision(x.precision), RoundingMode.HALF_UP)

        var result = x.value
        var factorial = BigDecimal(1.0)
        var factorial2 = BigDecimal(2.0)
        val four = BigDecimal(4.0)
        val xsquared = x.value.pow(2)
        var power = x.value

        // Taylor series x + x^3/6 + x^5/40 ...
        for(n in 1..1000) {
            // numerator
            power = power.multiply(xsquared, mc)                 // x^n
            if (n >= 2) {
                val i = n * 2
                factorial2 = factorial2.times(BigDecimal(i * (i - 1)))   // 2n!
            }

            // denominator
            val fourN = four.pow(n)
            factorial = factorial.times(BigDecimal(n))
            val factorialSquare = factorial.multiply(factorial)
            val twoNplus1 = BigDecimal((2 * n) + 1)

            var e = power.multiply(factorial2, mc)
            val denominator = fourN.multiply(factorialSquare, mc).multiply(twoNplus1, mc)
            e = e.divide(denominator, mc)

            val t = result.add(e, mc)
            if (t == result) {
                //println("iteration = $n")
                return RealBigDec(result, x.precision)
            }

            result = t
        }

        throw IterationLimitExceeded()
    }
}