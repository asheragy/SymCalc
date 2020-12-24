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

    throw IterationLimitExceeded()
}