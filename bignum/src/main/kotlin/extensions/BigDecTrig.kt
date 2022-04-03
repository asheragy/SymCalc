package org.cerion.math.bignum.extensions

import org.cerion.math.bignum.IterationLimitExceeded
import org.cerion.math.bignum.exp
import org.cerion.math.bignum.log
import org.cerion.math.bignum.sqrt
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


fun BigDecimal.sinh(precision: Int): BigDecimal {
    if (signum() == -1)
        return negate().sinh(precision).negate()
    else if (signum() == 0)
        return BigDecimal.ZERO

    val mc = MathContext(precision, RoundingMode.HALF_UP)

    if (toDouble() > 2.4) {
        // Reduce using Sinh(2x)= 2*Sinh(x)*Cosh(x)
        val xhalf = this.divide(BigDecimal(2), mc)
        return BigDecimal(2)
            .multiply(xhalf.sinh(precision), mc)
            .multiply(xhalf.cosh(precision), mc)
    }

    // This works too but not as efficient
    //return (Exp(x) - Exp(x.unaryMinus())) / Integer.TWO

    // Taylor series x + x^3/3! + x^5 / 7! + ...
    val xsquared = this.multiply(this, mc)
    var factorial = BigDecimal.ONE
    var xpow = this
    var result = this

    for(i in 1 until 100) {
        factorial = factorial.multiply(BigDecimal(i * 2), mc)
        factorial = factorial.multiply(BigDecimal(i * 2 + 1), mc)
        xpow = xpow.multiply(xsquared, mc)
        val term = xpow.divide(factorial, mc)

        val t = result.add(term, mc)
        if (t == result) // TODO in all other functions return t since its 1 iteration more accurate
            return t

        result = t
    }

    throw IterationLimitExceeded()
}

fun BigDecimal.cosh(precision: Int): BigDecimal {
    if (signum() == -1)
        return negate().cosh(precision)
    if (signum() == 0)
        return BigDecimal.ONE

    // Possible formula to use for faster convergence on larger numbers
    // cosh^2(x) = 1+ sinh^2(x)

    // Direct compute but slower
    // return (Exp(x) + Exp(x.unaryMinus())) / Integer.TWO

    val mc = MathContext(precision, RoundingMode.HALF_UP)
    val xsquared = this.multiply(this, mc)
    var term = BigDecimal.ONE
    var result = term

    for(n in 1 until 100) {
        term = term.multiply(xsquared, mc)
        term = term.divide(BigDecimal((2 * n) * (2 * n - 1)), mc)

        val t = result.add(term, mc)
        if (t == result)
            return t

        result = t
    }

    throw IterationLimitExceeded()
}

fun BigDecimal.tanh(precision: Int): BigDecimal {
    if (signum() == 0)
        return BigDecimal.ZERO
    else if (signum() == -1)
        return negate().tanh(precision).negate()

    val mc = MathContext(precision, RoundingMode.HALF_UP)
    val exp = (this.multiply(BigDecimal(2), mc)).exp(precision)
    return exp.subtract(BigDecimal.ONE, mc).divide(exp.add(BigDecimal.ONE, mc), mc)
}

fun BigDecimal.arcsin(precision: Int): BigDecimal {
    if (signum() == 0)
        return this
    if (signum() == -1)
        return negate().arcsin(precision).negate()

    val mc = MathContext(precision, RoundingMode.HALF_UP)

    this.sqrt(3)
    if (toDouble() > 0.7) {
        // Pi/2 - ArcSin(Sqrt(1 - x^2))
        val piOver2 = getPiToDigits(precision).divide(BigDecimal(2), mc)
        val oneMinusX2 = BigDecimal.ONE.subtract(this.pow(2, mc), mc)
        val sqrt1minusX2 = oneMinusX2.sqrt(mc) // TODO replace with custom version when fixed for zeros
        return piOver2.subtract(sqrt1minusX2.arcsin(precision), mc)
    }

    var result = this
    val xsquared = this.pow(2, mc)
    var xpow = this
    var numerator = BigDecimal(1.0)
    var denominator = BigDecimal(1.0)

    // Taylor series x + x^3/6 + x^5/40 ...
    for(n in 1..1000) {
        xpow = xpow.multiply(xsquared, mc)
        val n2 = (n * 2) - 1
        numerator = numerator.multiply(n2.toBigDecimal(), mc)

        val d1 = (n * 2)
        denominator = denominator.divide((d1 - 1).toBigDecimal(), mc)
        denominator = denominator.multiply((d1 * (d1+1)).toBigDecimal(), mc)

        var e = xpow.multiply(numerator, mc)
        e = e.divide(denominator, mc)

        val t = result.add(e, mc)
        if (t == result)
            return result

        result = t
    }

    throw IterationLimitExceeded()
}

fun BigDecimal.arccos(precision: Int): BigDecimal {
    val mc = MathContext(precision, RoundingMode.HALF_UP)

    // bypasses the need for calculating Pi/2
    if (toDouble() > 0.7) {
        val sqrt1minusX2 = BigDecimal.ONE.subtract(this.pow(2, mc), mc).sqrt(mc) // TODO use custom sqrt after zero fixed
        return sqrt1minusX2.arcsin(precision)
    }

    val piOver2 = getPiToDigits(precision).divide(BigDecimal(2), mc)
    return piOver2.subtract(this.arcsin(precision), mc)
}

fun BigDecimal.arctan(precision: Int): BigDecimal {
    if (signum() == -1)
        return negate().arctan(precision).negate()
    else if (signum() == 0)
        return BigDecimal.ZERO

    val mc = MathContext(precision, RoundingMode.HALF_UP)
    val dValue = toDouble()
    if (dValue > 0.7 && dValue < 3.0) {
        // Speed up convergence by using identity
        //   arctan(x) = 2*arctan( x / (1+ sqrt(1+x^2))
        val sqrt1plusX2 = this.pow(2, mc).add(BigDecimal.ONE, mc).sqrt(precision)
        val arctan = this.divide(BigDecimal(1).add(sqrt1plusX2, mc), mc).arctan(precision)
        return BigDecimal(2).multiply(arctan, mc)
    }
    else if(dValue < 0.71 ) {
        // TODO generic way to write taylor series since its used a lot
        // Basic taylor series, converges fast when < 0.7
        val xsquared = this.pow(2, mc).negate()
        var result = this
        var xpowi = this
        var denominator = BigDecimal.ONE

        for(i in 0 until 100) {
            xpowi = xpowi.multiply(xsquared, mc)
            denominator = denominator.add(BigDecimal(2), mc)
            val term = xpowi.divide(denominator, mc)

            val t = result.add(term, mc)
            if (result == t)
                return result

            result = t
        }

        throw IterationLimitExceeded()
    }
    else {
        // ArcTan(x >= 3.0) is close to Pi/2 and normal taylor series converges slower
        var result = getPiToDigits(precision).divide(BigDecimal(2), mc)

        var xpowi = BigDecimal(-1).divide(this, mc)
        val xsquared = xpowi.negate().multiply(xpowi, mc)
        var denominator = BigDecimal(-1)

        // TODO see if this can be rewritten to match the above one closer, loop starts a bit different
        for(i in 0 until 100) {
            denominator = denominator.add(BigDecimal(2), mc)
            val term = xpowi.divide(denominator, mc)

            val t = result.add(term, mc)
            if (result == t)
                return result

            result = t
            xpowi = xpowi.multiply(xsquared, mc)
        }

        throw IterationLimitExceeded()
    }
}

fun BigDecimal.arcsinh(precision: Int): BigDecimal {
    if (signum() == 0)
        return BigDecimal.ZERO

    val mc = MathContext(precision, RoundingMode.HALF_UP)
    val xsquare_plus1 = this.pow(2, mc).add(BigDecimal.ONE, mc)
    return xsquare_plus1.sqrt(precision).add(this, mc).log(precision)
}

fun BigDecimal.arccosh(precision: Int): BigDecimal {
    if (this.compareTo(BigDecimal.ONE) == 0)
        return BigDecimal.ZERO

    if (this < BigDecimal.ONE) {
        TODO("Add complex result or exception")
    }

    val mc = MathContext(precision, RoundingMode.HALF_UP)
    val xsquare_minus1 = this.pow(2, mc).subtract(BigDecimal.ONE, mc)
    return xsquare_minus1.sqrt(precision).add(this, mc).log(precision)
}