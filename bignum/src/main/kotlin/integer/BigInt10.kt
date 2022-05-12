package org.cerion.math.bignum.integer

import kotlin.math.absoluteValue
import kotlin.math.sign

@ExperimentalUnsignedTypes
class BigInt10 : BigIntArrayBase<BigInt10> {
    override val sign: Byte
    override val arr: UIntArray

    companion object {
        val ZERO = BigInt10(0, UIntArray(0))
        val ONE = BigInt10(1, UIntArray(1) { 1u })
        val NEGATIVE_ONE = BigInt10(-1, UIntArray(1) { 1u })
        val BASE = 1000000000u
    }

    constructor(s: Int, arr: UIntArray) : this (s.toByte(), arr)
    constructor(sign: Byte, arr: UIntArray) {
        this.sign = sign
        this.arr = arr

        validate()
    }

    constructor(n: Int) {
        sign = n.sign.toByte()
        arr = if (n == 0)
            UIntArray(0)
        else if (n > 999999999 || n < -999999999)
            uintArrayOf((n % 1000000000).absoluteValue.toUInt(), (n / 1000000000).absoluteValue.toUInt())
        else
            UIntArray(1) { n.absoluteValue.toUInt() }

        validate()
    }

    constructor(str: String) {
        val n = str.trimStart('-', '0')
        if (n.isEmpty()) {
            this.arr = UIntArray(0)
            this.sign = ZEROSIGN
            return
        }

        sign = if(str[0] == '-') NEGATIVE else POSITIVE

        // TODO this speed could be improved
        // declare array with exact size
        // loop in chunks of string length
        val digits = n.reversed().chunked(9).map { Integer.parseInt(it.reversed()).toUInt() }
        this.arr = digits.toUIntArray()

        validate()
    }

    private fun validate() {
        // TODO temp debugging
        for(digit in arr)
            if (digit > 999999999u)
                throw RuntimeException("invalid digit")
    }

    override fun toString(): String {
        if (sign == ZEROSIGN)
            return "0"

        val sb = StringBuilder()
        if (sign == NEGATIVE)
            sb.append("-")

        sb.append(arr[arr.size-1])

        for(i in arr.size-2 downTo  0) {
            val t = arr[i].toString()
            sb.append("0".repeat(9 - t.length) + t)
        }

        return sb.toString()
    }

    override fun toDouble(): Double {
        return when (arr.size) {
            0 -> 0.0
            1 -> arr[0].toDouble() * sign
            else -> {
                var result = arr.last().toDouble()
                for(i in arr.size-2 downTo 0) {
                    result *= BASE.toDouble()
                    result += arr[i].toDouble()
                }

                return result * sign
            }
        }
    }

    override fun toInt(): Int {
        return when (arr.size) {
            0 -> 0
            1 -> arr[0].toInt() * sign
            2 -> {
                val longValue = (arr[0] + (arr[1]* BASE.toULong())).toLong() * sign
                if (longValue > Integer.MAX_VALUE || longValue < Integer.MIN_VALUE)
                    throw ArithmeticException()

                longValue.toInt()
            }
            else -> throw ArithmeticException()
        }
    }

    override fun getInstance(sign: Byte, arr: UIntArray) = BigInt10(sign, arr)

    override fun equals(other: Any?) = other is BigInt10 && sign == other.sign && arr.contentEquals(other.arr)

    override fun sqrtRemainder(): Pair<BigInt10, BigInt10> {
        TODO("Not yet implemented")
    }

    override fun add(x: UIntArray, y: UIntArray): UIntArray = BigIntArray.add10(x, y)
    override fun subtract(x: UIntArray, y: UIntArray): UIntArray = BigIntArray.subtract10(x, y)

    override fun multiply(x: UIntArray, y: UInt): UIntArray {
        val result = UIntArray(x.size + 1) // Allocate carried digit by default
        var t = 0uL
        var i = 0

        while(i < x.size) {
            t = (x[i].toULong() * y) + (t / BASE)
            result[i++] = (t % BASE).toUInt()
        }

        result[x.size] = (t / BASE).toUInt()
        return BigIntArray.removeLeadingZeros(result)
    }

    override fun multiply(x: UIntArray, y: UIntArray): UIntArray {
        val xlen = x.size
        val ylen = y.size
        val z = UIntArray(xlen + ylen)

        var carry = 0uL
        var j = 0
        while (j < ylen) {
            val product = y[j].toULong() * x[0].toULong() + carry
            z[j] = (product % BASE).toUInt()
            carry = (product / BASE)
            j++
        }

        z[ylen] = (carry % BASE).toUInt()

        var i = 1
        while (i < xlen) {
            carry = 0uL
            j = 0
            var k = i
            while (j < ylen) {
                val product = y[j].toULong() * x[i].toULong() + z[k].toULong() + carry
                z[k] = (product % BASE).toUInt()
                carry = (product / BASE)
                j++
                k++
            }
            z[ylen + i] = (carry % BASE).toUInt()
            i++
        }

        return BigIntArray.removeLeadingZeros(z)
    }
}