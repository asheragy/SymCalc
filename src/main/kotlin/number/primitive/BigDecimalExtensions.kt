package org.cerion.symcalc.number.primitive

import org.cerion.symcalc.exception.IterationLimitExceeded
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.*


fun BigDecimal.exp(precision: Int): BigDecimal {
    val mc = MathContext(precision, RoundingMode.HALF_UP)

    if (signum() < 0) {
        val denominator = this.unaryMinus().exp(precision)
        return BigDecimal.ONE.divide(denominator, mc)
    }

    // Reduce number to <1 for faster convergence, exp(x) = exp(0.1*x)^10
    if (this > BigDecimal.ONE) {
        val n = min(1000000000, ceil(log10(toDouble())).toInt())
        val tenToN = 10.0.pow(n.toDouble()).toInt()
        return scaleByPowerOfTen(-n).exp(precision).pow(tenToN, mc)
    }

    // Maclaurin series 1 + x + x^2/2! + x^3/3! + ...
    var result = this.plus(BigDecimal.ONE)
    var factorial = BigDecimal.ONE
    var power = this

    for(i in 2..10000) {
        power = power.multiply(this, mc)
        factorial = factorial.multiply(BigDecimal(i))
        var term = power.divide(factorial, mc)
        term = result.add(term, mc)

        if (result == term)
            return result

        result = term
    }

    throw IterationLimitExceeded()
}

// TODO compare performance with java 9 version (when android can use it)
fun BigDecimal.sqrt(precision: Int): BigDecimal {
    if (signum() < 0)
        throw Exception("sqrt() on negative number")

    val initial = sqrt(toDouble())
    val mc = MathContext(precision)
    var xn = BigDecimal(initial)
    val two = BigDecimal(2)

    // Babylonian method
    // TODO_LP compare with Bakhshali method
    for(i in 0 until 1000) {
        val t = xn.add(this.divide(xn, mc), mc).divide(two, mc)

        if (t == xn)
            return t

        xn = t
    }

    throw IterationLimitExceeded()
}

fun BigDecimal.root(n: Int, precision: Int): BigDecimal {
    if (signum() < 0)
        throw Exception("sqrt() on negative number")
    if (n <= 0)
        throw ArithmeticException("Nth root must be >= 1")
    if (n == 1)
        return this

    // Newton method
    val mc = MathContext(precision)
    var xn = BigDecimal(toDouble().pow(1.0 / n))
    val nminus1 = BigDecimal(n - 1)
    val nbd = BigDecimal(n)

    for (i in 0 until 1000) {
        val a = xn.multiply(nminus1, mc)
        val b = this.divide(xn.pow(n-1, mc), mc)
        val c = a + b

        val t = c.divide(nbd, mc)
        if (t == xn)
            return t

        xn = t
    }

    throw IterationLimitExceeded()
}

fun BigDecimal.log(): BigDecimal = log(precision())

fun BigDecimal.log(precision: Int): BigDecimal {
    val mc = MathContext(precision, RoundingMode.HALF_UP)

    // Reduce range to [0.7,1.3]
    if (toDouble() > 1.3 && log2digits -2 > mc.precision) {
        var n = 0
        var dvalue = toDouble()
        while (dvalue >= 1.3) {
            n++
            dvalue /= 2
        }

        val a = this.divide(BigDecimal(2).pow(n, mc), mc)
        val log2 = getlog2(precision)
        val loga = logTaylorSeriesV2(a, mc)
        val nlog2 = log2.multiply(BigDecimal(n), mc)
        return loga.add(nlog2, mc)
    }

    // TODO on really small numbers this one works better by increasing the value
    // See if there is a better option in those cases
    else {
        if (abs(toDouble() - 1.0) > 0.3) {
            // Log(x) = n * Log(x^1/n)

            val n = Math.max(2, (ln(toDouble()) / 0.2).toInt())
            val root = root(n, precision)
            return logTaylorSeriesV2(root, mc).multiply(BigDecimal(n), mc)
        }
    }

    return logTaylorSeriesV2(this, mc)
}

private fun logTaylorSeriesV2(x: BigDecimal, mc: MathContext): BigDecimal {
    val xminus1 = x.subtract(BigDecimal.ONE)
    val xplus1 = x.add(BigDecimal.ONE)
    val xminus_over_xplus = xminus1.divide(xplus1, mc)
    val pow = xminus_over_xplus.multiply(xminus_over_xplus, mc)
    val c = xminus_over_xplus.multiply(BigDecimal(2), mc)

    var powTerm = BigDecimal.ONE
    var sum = BigDecimal.ONE

    for (k in 1 until 50000) {
        powTerm = powTerm.multiply(pow, mc)
        val kterm = BigDecimal.ONE.divide(BigDecimal(2 * k + 1), mc)

        val curr = powTerm.multiply(kterm, mc)
        val t = sum.add(curr, mc)
        if (sum == t)
            return t.multiply(c, mc)

        sum = t
    }

    throw IterationLimitExceeded()
}


/* Updated version is a bit faster on larger input
private fun logTaylorSeries(input: BigDecimal, mc: MathContext): BigDecimal {
    val x = input.subtract(BigDecimal(1))
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
 */

private fun getlog2(precision: Int): BigDecimal {
    if (log2digits -2 >= precision)
        return BigDecimal(LOG2_1000_DIGITS.substring(0, 2 + precision))

    throw ArithmeticException()
}

private val log2digits: Int
    get() = LOG2_1000_DIGITS.length

private const val LOG2_1000_DIGITS = "0.69314718055994530941723212145817656807550013436025525412068000949339" +
        "3621969694715605863326996418687542001481020570685733685520235758130557" +
        "0326707516350759619307275708283714351903070386238916734711233501153644" +
        "9795523912047517268157493206515552473413952588295045300709532636664265" +
        "4104239157814952043740430385500801944170641671518644712839968171784546" +
        "9570262716310645461502572074024816377733896385506952606683411372738737" +
        "2292895649354702576265209885969320196505855476470330679365443254763274" +
        "4951250406069438147104689946506220167720424524529612687946546193165174" +
        "6813926725041038025462596568691441928716082938031727143677826548775664" +
        "8508567407764845146443994046142260319309673540257444607030809608504748" +
        "6638523138181676751438667476647890881437141985494231519973548803751658" +
        "6127535291661000710535582498794147295092931138971559982056543928717000" +
        "7218085761025236889213244971389320378439353088774825970171559107088236" +
        "8362758984258918535302436342143670611892367891923723146723217205340164" +
        "92568727477823445353476"