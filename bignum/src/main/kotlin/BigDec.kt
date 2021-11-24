package org.cerion.math.bignum

import kotlin.math.abs
import kotlin.math.max

@ExperimentalUnsignedTypes
class BigDec {

    val value: BigInt
    val scale: Int

    constructor(value: BigInt, scale: Int) {
        this.value = value
        this.scale = scale
    }

    constructor(str: String) {
        val index = str.indexOf(".")
        if (index == -1)
            scale = 0
        else
            scale = str.length - index - 1

        value = BigInt(str.replace(".", ""))
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
        val pow = BigInt("10").pow(diff)
        val scaled = this.value * pow

        return BigDec(scaled + other.value, other.scale)
    }

    operator fun minus(other: BigDec): BigDec {
        if (scale == other.scale)
            return BigDec(value - other.value, scale)

        val diff = abs(other.scale - scale)
        val pow = BigInt("10").pow(diff)

        val subtracted = if(scale < other.scale)
            (this.value * pow) - other.value
        else
            value - (other.value * pow)

        return BigDec(subtracted, max(scale, other.scale))
    }

    operator fun times(other: BigDec): BigDec {
        return BigDec(value * other.value, scale + other.scale)
    }
}