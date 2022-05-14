package org.cerion.math.bignum.integer

import kotlin.math.*


@ExperimentalUnsignedTypes
class BigInt2 : BigIntArrayBase<BigInt2> {
    override val sign: Byte
    override val arr: UIntArray

    companion object {
        val ZERO = BigInt2(0, UIntArray(0))
        val ONE = BigInt2(1, UIntArray(1) { 1u })
        val NEGATIVE_ONE = BigInt2(-1, UIntArray(1) { 1u })
        private const val POSITIVE = (1).toByte()
        private const val NEGATIVE = (-1).toByte()
        private const val ZEROSIGN = 0.toByte()
    }

    constructor(s: Int, arr: UIntArray) : this (s.toByte(), arr)
    constructor(sign: Byte, arr: UIntArray) {
        this.sign = sign
        this.arr = arr
    }

    constructor(n: Int) {
        sign = n.sign.toByte()
        arr = if (n == 0)
            UIntArray(0)
        else
            UIntArray(1) { n.absoluteValue.toUInt() }
    }

    constructor(str: String) {
        val n = str.trimStart('-', '0')
        if (n.isEmpty()) {
            this.arr = UIntArray(0)
            this.sign = ZEROSIGN
            return
        }

        sign = if(str[0] == '-') NEGATIVE else POSITIVE

        val firstDigits = n.substring(0, if (n.length % 9 == 0) 9 else n.length % 9)
        val remainingDigits = n.substring(firstDigits.length).chunked(9).map { Integer.parseInt(it).toUInt() }

        val base = 1000000000
        val result = mutableListOf<UInt>()

        result.add(Integer.parseInt(firstDigits).toUInt())
        for(i in remainingDigits.indices) {
            // multiply entire result array by base
            var product: ULong = 0uL
            for(j in result.indices) {
                product = result[j] * base.toULong() + product.toShiftedUInt()
                result[j] = product.toUInt()
            }

            if (product.toShiftedUInt() != 0u)
                result.add(product.toShiftedUInt())

            // Add current digit
            product = result[0].toULong() + remainingDigits[i]
            result[0] = product.toUInt()

            // Carry as necessary
            if (product.toShiftedUInt() != 0u) {
                if (result.size == 1)
                    result.add(product.toShiftedUInt())
                else {
                    var index = 1
                    // TODO fix to match addition better
                    while(product.toShiftedUInt() != 0u) {
                        if (index == result.size) {
                            result.add(1u)
                            break
                        }
                        else {
                            product = result[index].toULong() + product.toShiftedUInt()
                            result[index++] = product.toUInt()
                        }
                    }
                }
            }
        }

        this.arr = result.toUIntArray()

    }

    /*
    override fun toBigDecimal(): BigDecimal = toBigInteger().toBigDecimal()
    override fun toBigInteger(): BigInteger {
        val constructor = BigInteger::class.constructors.toList()[11] // Signum / int[]
        constructor.isAccessible = true

        val copy = arr.map { it.toInt() }.reversed().toIntArray()
        return constructor.call(sign, copy)
    }
     */

    fun validate() {
        // Temp error checking
        if (this.sign != ZEROSIGN) {
            if (arr.isEmpty() || (arr.size == 1 && arr[0] == 0u))
                throw IllegalArgumentException("Sign should be zero")
        }
        else if (arr.isNotEmpty() && arr.all { it == 0u })
            throw IllegalArgumentException("Sign should NOT zero")
    }

    override fun getInstance(sign: Byte, arr: UIntArray) = BigInt2(sign, arr)
    override fun getInstance(value: String) = BigInt2(value)

    override fun toDouble(): Double {
        return when (arr.size) {
            0 -> 0.0
            1 -> arr[0].toDouble() * sign
            else -> {
                var result = arr.last().toDouble()
                for(i in arr.size-2 downTo 0) {
                    result *= 4294967296.0
                    result += arr[i].toDouble()
                }

                return result * sign
            }
        }
    }

    override fun toInt(): Int {
        return when (arr.size) {
            0 -> 0
            1 -> {
                // Special case since unsigned negative is 1 higher than max value unsigned
                if (sign == NEGATIVE && arr[0] == Integer.MIN_VALUE.toUInt())
                    return Integer.MIN_VALUE

                if (arr[0] > Integer.MAX_VALUE.toUInt())
                    throw ArithmeticException()

                arr[0].toInt() * sign
            }
            else -> throw ArithmeticException()
        }
    }

    override fun modPow(exponent: BigInt2, m: BigInt2): BigInt2 {
        if (exponent == ZERO)
            return ONE

        var result = ONE
        var square = this
        for(i in exponent.arr.indices) {
            var n = exponent.arr[i]
            repeat(32) {
                if (n % 2u == 1u) {
                    val a = square * result
                    val b = a.mod(m)
                    result = b
                }

                square = (square * square).mod(m)
                n /= 2u
            }
        }

        return result
    }

    override fun toString(): String {
        if (sign == ZEROSIGN)
            return "0"

        val sb = StringBuilder()

        if (arr.size == 1) {
            sb.append(arr[0])
        }
        else {
            var remaining = arr
            while (remaining.size > 1) {
                val t = divide(remaining, 1000000000u)
                remaining = t.first
                sb.insert(0, "0".repeat(9 - t.second.toString().length) + t.second.toString())
            }

            if (remaining[0] != 0u)
                sb.insert(0, remaining[0])
        }

        if (sign == NEGATIVE)
            sb.insert(0, "-")

        return sb.toString()
    }

    override fun shiftLeft(n: UInt): BigInt2 {
        // TODO improve this to do real shift
        val pow = BigInt2(2).pow(n.toInt())
        return this.times(pow)
    }

    override fun shiftRight(n: UInt): BigInt2 {
        if (n == 0u)
            return this

        val shift = (n % 32u).toInt()
        val drop = (n / 32u).toInt()
        if (drop >= arr.size)
            return ZERO

        val newArr = arr.drop(drop).toUIntArray()
        val twoPow = 2.0.pow(shift.toDouble()).toUInt()

        val result = divide(newArr, twoPow).first
        return BigInt2(sign, result)
    }

    override fun sqrtRemainder(): Pair<BigInt2, BigInt2> {
        if (sign == BigIntArrayBase.NEGATIVE)
            throw ArithmeticException()

        // Newton method, get initial estimate
        val bl = bitLength
        var x = if (bl > 120u)
            shiftRight(bl / 2u - 1u)
        else
            BigInt2("" + sqrt(toDouble()).roundToLong())

        val two = BigInt2(2)
        while (true) {
            val x2 = x.pow(2)
            var xplus2 = x.add(ONE).pow(2)

            if (x2 <= this && xplus2 > this)
                break

            xplus2 = xplus2.subtract(x.shiftLeft(2u))
            if (xplus2 <= this && x2 > this) {
                x = x.subtract(ONE)
                break
            }

            xplus2 = x2.subtract(this).divide(x).divide(two)
            x = x.subtract(xplus2)
        }

        val remainder = this - (x * x)
        return Pair(x, remainder)
    }

    override val bitLength: UInt
        get() {
            if (arr.isEmpty())
                return 0u

            return ((arr.size - 1) * 32).toUInt() + arr.last().bitLength()
        }

    //region array operations

    override fun add(x: UIntArray, y: UIntArray): UIntArray {
        val a = if (x.size >= y.size) x else y
        val b = if (x.size >= y.size) y else x

        val arr = UIntArray(a.size)
        var index = 0
        var sum: ULong = 0uL

        // Add digits while array lengths are the same
        while(index < b.size) {
            sum = a[index].toULong() + b[index].toULong() + (sum shr 32)
            arr[index++] = sum.toUInt()
        }

        // Add digits for larger number while digit is still carried
        var carry = sum shr 32 != 0uL
        while(index < a.size && carry) {
            sum = a[index].toULong() + (sum shr 32)
            carry = sum shr 32 != 0uL
            arr[index++] = sum.toUInt()
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
        var diff = 0L

        while(index < y.size) {
            diff = x[index].toLong() - y[index].toLong() + (diff shr 32)
            arr[index++] = diff.toUInt()
        }

        var borrow = diff < 0
        while(index < x.size && borrow) {
            diff = x[index].toLong() - 1
            borrow = diff < 0
            arr[index++] = diff.toUInt()
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
            t = (x[i].toULong() * y) + t.toShiftedUInt()
            result[i++] = t.toUInt()
        }

        result[x.size] = t.toShiftedUInt()
        return removeLeadingZeros(result)
    }

    override fun multiply(x: UIntArray, y: UIntArray): UIntArray {
        val xlen = x.size
        val ylen = y.size
        val z = UIntArray(xlen + ylen)

        var carry = 0uL
        var j = 0
        while (j < ylen) {
            try {
                val product = y[j].toULong() * x[0].toULong() + carry
                z[j] = product.toUInt()
                carry = product shr 32
                j++
            }
            catch(e: Exception) {
                println("TEST")
            }
        }

        z[ylen] = carry.toUInt()

        var i = 1
        while (i < xlen) {
            carry = 0uL
            j = 0
            var k = i
            while (j < ylen) {
                val product = y[j].toULong() * x[i].toULong() + z[k].toULong() + carry
                z[k] = product.toUInt()
                carry = product shr 32
                j++
                k++
            }
            z[ylen + i] = carry.toUInt()
            i++
        }

        return removeLeadingZeros(z)
    }

    override fun divide(x: UIntArray, y: UInt): Pair<UIntArray, UInt> {
        var index = x.size - 1
        var r = if (x.last() / y == 0u)
            (x[index--].toULong() shl 32)
        else
            0uL

        val result = UIntArray(index+1)

        while(index >= 0) {
            val t = r + x[index]
            result[index] = (t / y).toUInt()
            r = (t % y) shl 32
            index--
        }

        return Pair(removeLeadingZeros(result), r.toShiftedUInt())
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
            (n[index--].toULong() shl 32)
        else
            0uL

        val result = UIntArray(index+1-offset)
        while(index >= offset) {
            val t = r + n[index]
            result[index-offset] = (t / y).toUInt()
            r = (t % y) shl 32
            index--
        }

        return removeLeadingZeros(result)
    }
    //endregion
}

// Returns larger half as UInt
inline fun ULong.toShiftedUInt(): UInt = (this shr 32).toUInt()
inline fun UInt.bitLength(): UInt = (ln(this.toDouble()) / ln(2.0)).toUInt() + 1u