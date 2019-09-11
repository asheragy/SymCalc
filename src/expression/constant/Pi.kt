package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.nevec.rjm.BigDecimalMath
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class Pi : ConstExpr() {
    override fun toString(): String = "Pi"

    override fun evaluate(): Expr {
        return evaluate(InfinitePrecision)
    }

    override fun evaluate(precision: Int): Expr {
        if (precision < InfinitePrecision) {
            if (precision == MachinePrecision)
                return RealDouble(Math.PI)

            return RealBigDec( getPiToDigits(precision) )
        }
        else
            return this
    }

    private fun getPiToDigits(precision: Int): BigDecimal {
        // https://en.wikipedia.org/wiki/Chudnovsky_algorithm
        // 426880*Sqrt(10005)/Pi = Sum (6k!)(545140134k+13591409) / (3k)!(k!)^3(-262537412640768000)^k

        val mc = MathContext(precision + 10, RoundingMode.HALF_UP)
        val bd54 = BigDecimal(545140134)
        val bd13 = BigDecimal(13591409)
        val bd26 = BigDecimal("-262537412640768000")

        // Calculate sum
        var sum = BigDecimal(0)
        for(k in 0..100) {
            val n1 = factorial(6 * k)
            var numerator = bd54.multiply(BigDecimal(k))
            numerator = numerator.add(bd13)
            numerator = numerator.multiply(n1)

            val d1 = factorial(3 * k)
            val d2 = factorial(k).pow(3)
            val d3 = bd26.pow(k)
            val denominator = d1.multiply(d2).multiply(d3)

            val next = numerator.divide(denominator, mc)
            val t = sum.add(next, mc)
            if (t == sum) {
                // Divide this constant value by sum to get Pi
                var c = BigDecimalMath.sqrt(BigDecimal("10005"), mc)
                c = c.multiply(BigDecimal("426880"))

                return c.divide(sum, MathContext(precision, RoundingMode.HALF_UP))
            }

            sum = t
        }

        throw IterationLimitExceeded()
    }

    private fun factorial(k: Int): BigDecimal {
        var bd = BigDecimal(1.0)
        for(i in 1..k) {
            bd = bd.multiply(BigDecimal(i))
        }

        return bd
    }

    /*
    private var one: BigDecimal = BigDecimal.ZERO
    private var two: BigDecimal = BigDecimal.ZERO
    private var four: BigDecimal = BigDecimal.ZERO

    private fun getPiToDigits2(scale: Int): BigDecimal {
        // Initialize
        one = BigDecimal(1.0).setScale(scale)
        two = BigDecimal(2.0).setScale(scale)
        four = BigDecimal(4.0).setScale(scale)

        var sum = calc(0, scale)

        // FEAT look into another algorithm this one is slow after 2000+ digits
        for(i in 1..10000) {
            val prev = sum
            sum += calc(i, scale)

            if (sum == prev) {
                return sum.setScale(scale - 1, RoundingMode.HALF_UP)
            }
        }

        throw RuntimeException("too many iterations required to calculate")
    }

    // 16-k [ 4/(8k+1) - 2/(8k+4) - 1/(8k+5) - 1/(8k+6) ]
    private fun calc(i: Int, scale: Int): BigDecimal {
        var k = BigDecimal(i).setScale(scale)
        val k8 = k.times(BigDecimal(8.0))

        var result = four / k8.plus(BigDecimal.ONE)
        result -= two / k8.plus(BigDecimal(4.0))
        result -= one / k8.plus(BigDecimal(5.0))
        result -= one / k8.plus(BigDecimal(6.0))

        k = BigDecimal(16).pow(i)
        k = BigDecimal.ONE.divide(k)
        result *= k

        return result.setScale(scale, RoundingMode.HALF_UP)
    }
     */
}
