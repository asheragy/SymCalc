package org.cerion.math.bignum

import org.cerion.math.bignum.integer.BigInt10
import kotlin.math.abs
import kotlin.math.max

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

        return BigDec(result, numberatorExtraDigits)
    }

    @Deprecated("need precision")
    operator fun div(other: BigDec): BigDec {
        val div = this.value.div(other.value)
        val newScale = scale
        val multiply = BigInt10("10").pow(newScale)

        return BigDec(div * multiply, scale)
    }

    val precision: Int
        get() {
            return value.digits
        }
}