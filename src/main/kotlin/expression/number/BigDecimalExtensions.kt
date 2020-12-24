package org.cerion.symcalc.expression.number

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.nevec.rjm.BigDecimalMath
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.max


fun BigDecimal.log(): BigDecimal {
    val mc = MathContext(precision(), RoundingMode.HALF_UP)

    // Reduce range to [0.7,1.3]
    if (abs(toDouble() - 1.0) > 0.3) {
        // Log(x) = n * Log(x^1/n)

        val n = max(2, (ln(toDouble()) / 0.2).toInt())
        val root = BigDecimalMath.root(n, this)
        return  root.log().multiply(BigDecimal(n), mc)
    }

    // Modified taylor series
    val xminus1 = this.subtract(BigDecimal.ONE)
    val xplus1 = this.add(BigDecimal.ONE)
    val xminus_over_xplus = xminus1.divide(xplus1, mc)
    val pow = xminus_over_xplus.multiply(xminus_over_xplus, mc)
    val c = xminus_over_xplus.multiply(BigDecimal(2), mc)

    var powTerm = BigDecimal.ONE
    var sum = BigDecimal.ONE

    for (k in 1 until 100) {
        powTerm = powTerm.multiply(pow, mc)
        val kterm = BigDecimal.ONE.divide(BigDecimal(2 * k + 1), mc)

        val curr = powTerm.multiply(kterm, mc)
        val t = sum.add(curr, mc)
        if (sum == t)
            return t.multiply(c, mc)

        sum = t
    }

    /* Standard taylor series which is a bit slower
    val x = this.subtract(BigDecimal(1))
    var sum = x
    var xpow = x

    for(i in 2 until 1000) {
        xpow = xpow.multiply(x, mc)
        val term = xpow.divide(BigDecimal(i), mc)

        val t = if (i % 2 == 0)
            sum.subtract(term, mc)
        else
            sum.add(term, mc)

        if (sum == t)
            return t

        sum = t
    }
     */

    throw IterationLimitExceeded()
}