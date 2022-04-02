package org.cerion.math.bignum.extensions

import org.cerion.math.bignum.IterationLimitExceeded
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


fun BigDecimal.sin(precision: Int): BigDecimal {
    val mc = MathContext(precision, RoundingMode.HALF_UP)

    // Normalize to range of 0 to 2pi
    val x2pi =
        if (toDouble() >= 0 && toDouble() < (2*Math.PI))
            this
        else
            remainder(getPiToDigits(precision) * BigDecimal(2))

    var result = x2pi
    var factorial = BigDecimal(1.0)
    val xsquared = x2pi.pow(2)
    var power = x2pi

    // Taylor series x - x^3/3! + x^5/5! ...
    for(i in 1..1000) {
        val n = (i * 2) + 1
        power = power.multiply(xsquared, mc)                 // x^n
        factorial = factorial.times(BigDecimal(n * (n-1)))   // n!
        val e = power.divide(factorial, mc)

        val t = if (i % 2 == 0)
            result.add(e, mc)
        else
            result.subtract(e, mc)

        if (t == result)
            return result

        result = t
    }

    throw IterationLimitExceeded()
}

fun BigDecimal.cos(precision: Int): BigDecimal {
    val mc = MathContext(precision, RoundingMode.HALF_UP)

    // Normalize to range of 0 to 2pi
    val x2pi =
        if (toDouble() >= 0 && toDouble() < (2*Math.PI))
            this
        else
            remainder(getPiToDigits(precision) * BigDecimal(2))

    var result = BigDecimal(1.0)
    var factorial = BigDecimal(1.0)
    val xsquared = x2pi.pow(2)
    var power = BigDecimal(1.0)

    // Taylor series 1 - x^2/2! + x^4/4! ...
    for(i in 1..1000) {
        val n = i * 2
        power = power.multiply(xsquared, mc)                 // x^n
        factorial = factorial.times(BigDecimal(n * (n-1)))   // n!
        val e = power.divide(factorial, mc)

        val t = if (i % 2 == 0)
            result.add(e, mc)
        else
            result.subtract(e, mc)

        if (t == result)
            return result

        result = t
    }

    throw IterationLimitExceeded()
}

fun BigDecimal.tan(precision: Int): BigDecimal {
    return sin(precision).divide(cos(precision),
        MathContext(precision, RoundingMode.HALF_UP))
}

/* Mostly working taylor series method, because of slow convergence and recursive bernoulli the sin/cos method seems to be much better
 override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
     val mc = MathContext(x.precision+5, RoundingMode.HALF_UP)

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

     throw IterationLimitExceeded()
 }
  */
