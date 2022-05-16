package org.cerion.math.bignum.integer

import kotlin.math.*

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

    // Less efficient than BigInt2 version since digits cannot be isolated
    override fun modPow(exponent: BigInt10, m: BigInt10): BigInt10 {
        if (exponent == ZERO)
            return ONE

        var result = ONE
        var square = this
        var current = exponent
        val two = BigInt10(2)

        while(current != ZERO) {
            val div = current.divideAndRemainder(two)
            if (div.second == ONE) {
                val a = square * result
                val b = a.mod(m)
                result = b
            }

            square = (square * square).mod(m)
            current = div.first
        }

        return result
    }

    override fun getInstance(sign: Byte, arr: UIntArray) = BigInt10(sign, arr)
    override fun getInstance(value: String) = BigInt10(value)

    override fun shiftRight(n: UInt): BigInt10 {
        val pow = BigInt10(2).pow(n.toInt())
        if (pow > this)
            return ZERO

        return this.divide(pow)
    }

    override fun shiftLeft(n: UInt): BigInt10 {
        val pow = BigInt10(2).pow(n.toInt())
        return this.times(pow)
    }

    // TODO this could be faster since its equivalent to bit shifting on normal numbers
    fun shift10(n: Int): BigInt10 {
        if (n == 0)
            return this
        if (n > 0)
            return this.times(BigInt10("10").pow(n))

        TODO("Not implemented")
    }

    val digits: Int
        get() {
            if (sign == ZEROSIGN)
                return 1

            return (arr.size - 1) * 10 + floor(log10(arr[arr.size - 1].toDouble())).toInt() + 1
        }

    override val bitLength: UInt
        get() {
            if (sign == ZEROSIGN)
                return 0u

            // TODO inefficient but unsure what else to do
            // 2^29 largest single digit we can divide by
            val divideBy = BigInt10(536870912)
            var remaining = this
            var length = 0u
            while(remaining > divideBy) {
                remaining = remaining.divide(divideBy)
                length += 29u
            }

            return length + remaining.arr[0].bitLength()
        }

    //region array operations

    override fun add(x: UIntArray, y: UIntArray): UIntArray {
        val a = if (x.size >= y.size) x else y
        val b = if (x.size >= y.size) y else x

        val arr = UIntArray(a.size)
        var index = 0
        var sum: UInt = 0u

        // Add digits while array lengths are the same
        while(index < b.size) {
            sum = a[index] + b[index] + (sum / 1000000000u)
            arr[index++] = sum % 1000000000u
        }

        // Add digits for larger number while digit is still carried
        var carry = sum >= 1000000000u
        while(index < a.size && carry) {
            sum = a[index] + (sum / 1000000000u)
            carry = sum >= 1000000000u
            arr[index++] = sum % 1000000000u
        }

        // If there is still a digit to carry add it and return
        if (carry) {
            val t = arr.copyOf(arr.size + 1)
            t[arr.size] = 1u
            return t
        }

        // If no more carry just add the remaining digits
        while(index < a.size)
            arr[index] = a[index++]

        return arr
    }

    override fun subtract(x: UIntArray, y: UIntArray): UIntArray {
        if(x < y)
            throw ArithmeticException("invalid subtraction") // TODO remove temp error checking

        // Input is always expected to have x as the largest array and adjust result sign accordingly
        val arr = UIntArray(x.size) // TODO is there a benefit in copying starting array? need to performance test first
        var index = 0
        var diff = 0

        while(index < y.size) {
            diff = x[index].toInt() - y[index].toInt() - if(diff < 0) 1 else 0
            arr[index++] = diff.toUInt() + if(diff < 0) 1000000000u else 0u
        }

        var borrow = diff < 0
        while(index < x.size && borrow) {
            diff = x[index].toInt() - 1
            borrow = diff < 0
            arr[index++] = diff.toUInt() + if(diff < 0) 1000000000u else 0u
        }

        while(index < x.size)
            arr[index] = x[index++]

        return removeLeadingZeros(arr)
    }

    override fun multiply(x: UIntArray, y: UInt): UIntArray {
        val result = UIntArray(x.size + 1) // Allocate carried digit by default
        var t = 0uL
        var i = 0

        while(i < x.size) {
            t = (x[i].toULong() * y) + (t / BASE)
            result[i++] = (t % BASE).toUInt()
        }

        result[x.size] = (t / BASE).toUInt()
        return removeLeadingZeros(result)
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

        return removeLeadingZeros(z)
    }

    override fun divide(x: UIntArray, y: UInt): Pair<UIntArray, UInt> {
        var index = x.size - 1
        var r = if (x.last() / y == 0u)
            (x[index--].toULong() * BASE)
        else
            0uL

        val result = UIntArray(index+1)

        while(index >= 0) {
            val t = r + x[index]
            result[index] = (t / y).toUInt()
            r = (t % y) * BASE
            index--
        }

        return Pair(removeLeadingZeros(result), (r / BASE).toUInt())
    }

    /**
     * Division with only first significant digit of divisor
     * ex: 88888/777 --> 88800/700 --> 888/7
     */
    override fun fastDivide(n: UIntArray, d: UIntArray): UIntArray {
        if (d.size > n.size)
            return UIntArray(0)

        val offset = d.size - 1
        val y = d.last()
        // For reference this is what needs to be computed
        //val n1 = n.toList().drop(offset).toUIntArray()
        //return  divideAndRemainder(n1, y).first

        var index = n.size - 1
        var r = if (n.last() / y == 0u)
            (n[index--].toULong() * BASE)
        else
            0uL

        val result = UIntArray(index+1-offset)
        while(index >= offset) {
            val t = r + n[index]
            result[index-offset] = (t / y).toUInt()
            // TODO unsure if toUInt above is always valid
            if (result[index-offset] >= BASE)
                throw RuntimeException("BigInt10.fastDivide error")
            r = (t % y) * BASE
            index--
        }

        return removeLeadingZeros(result)
    }
    //endregion
}