package org.cerion.math.bignum.extensions

import org.cerion.math.bignum.IterationLimitExceeded
import org.cerion.math.bignum.sqrt
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


fun getPiToDigits(precision: Int): BigDecimal {
    // https://en.wikipedia.org/wiki/Chudnovsky_algorithm
    // 426880*Sqrt(10005)/Pi = Sum (6k!)(545140134k+13591409) / (3k)!(k!)^3(-262537412640768000)^k

    val mc = MathContext(precision, RoundingMode.HALF_UP)
    val bd54 = BigDecimal(545140134)
    val bd13 = BigDecimal(13591409)
    val bd26 = BigDecimal("-262537412640768000")

    // Calculate sum
    var sum = bd13
    var n1 = BigDecimal.ONE // (6*k)!
    var d1 = BigDecimal.ONE // (3*k)!
    var d2 = BigDecimal.ONE // k!
    var d3 = BigDecimal.ONE // bd26^k
    for(k in 1..5000) {
        n1 = factorial(6*k, n1, 6*(k-1))
        val numerator = bd54
            .multiply(BigDecimal(k))
            .add(bd13)
            .multiply(n1)

        d1 = factorial(3 * k, d1, 3 * (k-1))
        d2 = factorial(k, d2, k-1)
        d3 = d3.multiply(bd26, mc)
        val denominator = d1.multiply(d2.pow(3)).multiply(d3)

        val next = numerator.divide(denominator, mc)
        val t = sum.add(next, mc)
        if (t == sum) {
            // Divide this constant value by sum to get Pi
            var c = BigDecimal("10005").sqrt(mc.precision)
            c = c.multiply(BigDecimal("426880"))

            return c.divide(sum, mc)
        }

        sum = t
    }

    throw IterationLimitExceeded()
}

private fun factorial(k: Int, prev: BigDecimal, kprev: Int): BigDecimal {
    var bd = prev
    for(i in (kprev+1)..k) {
        bd = bd.multiply(BigDecimal(i))
    }

    return bd
}