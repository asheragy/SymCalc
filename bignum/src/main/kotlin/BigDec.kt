package org.cerion.math.bignum

import org.cerion.math.bignum.integer.BigInt10
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.sign

@ExperimentalUnsignedTypes
class BigDec {

    val value: BigInt10
    val scale: Int

    constructor(value: BigInt10, scale: Int) {
        this.value = value
        this.scale = scale
    }

    constructor(str: String) {
        val index = str.indexOf(".")
        if (index == -1)
            scale = 0
        else
            scale = str.length - index - 1

        value = BigInt10(str.replace(".", ""))
    }

    constructor(n: Int) {
        scale = 0
        value = BigInt10(n)
    }

    constructor(x: Double) {
        // TODO
        scale = 0
        value = BigInt10(x.toInt())
    }

    override fun toString(): String {
        if (scale == 0)
            return value.toString()

        val result = StringBuilder(value.toString())
        if(result.length < scale)
            result.insert(0, "0".repeat(scale - result.length + 1))
        result.insert(result.length - scale, ".")
        return result.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other is BigDec)
            return scale == other.scale && value == other.value

        return false
    }

    operator fun plus(other: BigDec): BigDec {
        if (scale == other.scale)
            return BigDec(value + other.value, scale)

        if (scale > other.scale)
            return other + this

        val diff = other.scale - scale
        val pow = BigInt10("10").pow(diff)
        val scaled = this.value * pow

        return BigDec(scaled + other.value, other.scale)
    }

    operator fun minus(other: BigDec): BigDec {
        if (scale == other.scale)
            return BigDec(value - other.value, scale)

        val diff = abs(other.scale - scale)
        val pow = BigInt10("10").pow(diff)

        val subtracted = if(scale < other.scale)
            (this.value * pow) - other.value
        else
            value - (other.value * pow)

        return BigDec(subtracted, max(scale, other.scale))
    }

    operator fun times(other: BigDec): BigDec {
        return BigDec(value * other.value, scale + other.scale)
    }

    fun divide(other: BigDec, newPrecision: Int): BigDec {
        var numberatorExtraDigits = newPrecision - (precision - other.precision)

        // Test if scale is off by 1
        // TODO probably a faster way to do this check
        val denominatorScaled = other.value.shift10(precision - other.precision)
        if (value.abs() > denominatorScaled.abs())
            numberatorExtraDigits--

        val numerator = this.value.shift10(numberatorExtraDigits)
        var (result, rem) = numerator.divideAndRemainder(other.value)

        // TODO this could be improved, full division could be avoided
        // If remainder > divisor/2 round up by adding 1
        if(rem.digits == other.value.digits - 1 || rem.digits == other.value.digits) {
            val half = other.value.div(BigInt10(2))
            if (rem > half)
                result = result.add(BigInt10(1))
        }

        return BigDec(result, this.scale - other.scale + numberatorExtraDigits)
    }

    fun divide(other: BigDec, mc: MathContext): BigDec {
        return this.divide(other, mc.precision)
    }

    @Deprecated("need precision")
    operator fun div(other: BigDec): BigDec {
        val div = this.value.div(other.value)
        val newScale = scale
        val multiply = BigInt10("10").pow(newScale)

        return BigDec(div * multiply, scale)
    }

    fun sqrt(precision: Int): BigDec {
        if (value.signum() < 0)
            throw Exception("sqrt() on negative number")

        // TODO toDouble needs to be on the bigDec value
        val initial = kotlin.math.sqrt(toDouble())
        val mc = MathContext(precision)
        var xn = BigDec(initial)
        val two = BigDec(2)

        // Babylonian method
        // TODO_LP compare with Bakhshali method
        for(i in 0 until 1000) {
            val t = xn.plus(this.divide(xn, mc)).divide(two, mc)

            if (t == xn)
                return t

            xn = t
        }

        throw IterationLimitExceeded()
    }

    fun toDouble(): Double {
        return value.toDouble()
    }

    val precision: Int
        get() {
            return value.digits
        }


    companion object {
        val ONE = BigDec(1)

        /*
        fun getPiToDigits(precision: Int): BigDec {
            // https://en.wikipedia.org/wiki/Chudnovsky_algorithm
            // 426880*Sqrt(10005)/Pi = Sum (6k!)(545140134k+13591409) / (3k)!(k!)^3(-262537412640768000)^k

            val mc = MathContext(precision, RoundingMode.HALF_UP)
            val bd54 = BigDec(545140134)
            val bd13 = BigDec(13591409)
            val bd26 = BigDec("-262537412640768000")

            // Calculate sum
            var sum = bd13
            var n1 = ONE // (6*k)!
            var d1 = ONE // (3*k)!
            var d2 = ONE // k!
            var d3 = ONE // bd26^k
            for (k in 1..5000) {
                n1 = factorial(6 * k, n1, 6 * (k - 1))
                val numerator = bd54
                    .times(BigDec(k))
                    .plus(bd13)
                    .times(n1)

                d1 = factorial(3 * k, d1, 3 * (k - 1))
                d2 = factorial(k, d2, k - 1)
                d3 = d3.times(bd26, mc)
                val denominator = d1.times(d2.pow(3)).times(d3)

                val next = numerator.divide(denominator, mc)
                val t = sum.plus(next)
                if (t == sum) {
                    // Divide this constant value by sum to get Pi
                    var c = BigDec("10005").sqrt(mc.precision)
                    c = c.multiply(BigDec("426880"))

                    return c.divide(sum, mc)
                }

                sum = t
            }

            throw IterationLimitExceeded()
        }
         */

        private fun factorial(k: Int, prev: BigDec, kprev: Int): BigDec {
            var bd = prev
            for (i in (kprev + 1)..k) {
                bd = bd.times(BigDec(i))
            }

            return bd
        }
    }
}