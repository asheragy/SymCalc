package org.cerion.math.bignum.integer

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.*
import kotlin.reflect.jvm.isAccessible


@ExperimentalUnsignedTypes
class BigInt2 : IBigInt, BigIntArrayBase<BigInt2> {
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

    override fun toBigDecimal(): BigDecimal = toBigInteger().toBigDecimal()
    override fun toBigInteger(): BigInteger {
        val constructor = BigInteger::class.constructors.toList()[11] // Signum / int[]
        constructor.isAccessible = true

        val copy = arr.map { it.toInt() }.reversed().toIntArray()
        return constructor.call(sign, copy)
    }

    fun validate() {
        // Temp error checking
        if (this.sign != ZEROSIGN) {
            if (arr.isEmpty() || (arr.size == 1 && arr[0] == 0u))
                throw IllegalArgumentException("Sign should be zero")
        }
        else if (arr.isNotEmpty() && arr.all { it == 0u })
            throw IllegalArgumentException("Sign should NOT zero")
    }

    // Operators
    operator fun div(other: BigInt2): BigInt2 = this.divide(other)
    operator fun div(other: IBigInt): BigInt2 = this.divide(other as BigInt2)

    override fun getInstance(sign: Byte, arr: UIntArray) = BigInt2(sign, arr)

    override fun divide(other: IBigInt) = this.divide(other as BigInt2)
    fun divide(other: BigInt2): BigInt2 = divideAndRemainder(other).first as BigInt2

    fun divide(other: UInt): BigInt2 {
        return BigInt2(1, BigIntArray.divideAndRemainder(arr, other).first)
    }

    override fun equals(other: Any?) = other is BigInt2 && sign == other.sign && arr.contentEquals(other.arr)
    override fun signum() = sign.toInt()

    override fun testBit(n: Int): Boolean {
        if (n != 0)
            throw Exception() // only supporting to check for odd

        if (sign == ZEROSIGN)
            return false

        return arr[0] % 2u != 0u
    }

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

                return result
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

    override fun gcd(n: IBigInt): IBigInt {
        n as BigInt2
        if (this.sign == ZEROSIGN)
            return n
        else if (n.sign == ZEROSIGN)
            return this

        // A is the larger of the two numbers
        var a: UIntArray
        var b: UIntArray
        when(BigIntArray.compare(arr, n.arr)) {
            1 -> { a = arr; b = n.arr }
            -1 -> { b = arr; a = n.arr }
            else -> return this.abs()
        }

        var c = BigIntArray.divide(a, b)

        // No remainder, GCD is B
        while (c.second != null) {
            a = b
            b = c.second!!
            c = BigIntArray.divide(a, b)
        }

        return BigInt2(1, b)
    }

    override fun mod(m: IBigInt): IBigInt {
        m as BigInt2
        val rem = this.divideAndRemainder(m).second as BigInt2

        if (rem.sign == NEGATIVE)
            return m + rem

        return rem
    }

    override fun modPow(exponent: IBigInt, m: IBigInt): IBigInt {
        exponent as BigInt2
        m as BigInt2

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
                    result = b as BigInt2
                }

                square = (square * square).mod(m) as BigInt2
                n /= 2u
            }
        }

        return result
    }

    override fun divideAndRemainder(other: IBigInt): Pair<IBigInt, IBigInt> {
        other as BigInt2
        var sign = (sign * other.sign)

        if (other.arr.size == 1) {
            val result = BigIntArray.divideAndRemainder(arr, other.arr[0])
            val remainder = BigInt2(sign, UIntArray(1) { result.second })

            return Pair(BigInt2(sign, result.first), remainder)
        }

        val div = if (BigIntArray.compare(arr, other.arr) < 0) // x/y = 0 rem x when x<y
            Pair(UIntArray(0), arr)
        else
            BigIntArray.divide(this.arr, other.arr)

        val rem = when {
            div.second == null -> ZERO
            sign == -1 -> BigInt2(NEGATIVE, div.second!!)
            else -> BigInt2(POSITIVE, div.second!!)
        }

        if (div.first.isEmpty())
            sign = 0

        return Pair(BigInt2(sign, div.first), rem)
    }

    override fun sqrtRemainder(): Pair<BigInt2, BigInt2> {
        if (sign == NEGATIVE)
            throw ArithmeticException()

        // Newton method, get initial estimate
        val bl = bitLength
        var x = if (bl > 120u)
            shiftRight(bl / 2u - 1u)
        else
            BigInt2("" + sqrt(toDouble()).roundToLong())

        val two = BigInt2(2)
        while (true) {
            val x2 = x.pow(2) as BigInt2
            var xplus2 = x.add(ONE).pow(2) as BigInt2

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
                val t = BigIntArray.divideAndRemainder(remaining, 1000000000u)
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

    override fun isProbablePrime(certainty: Int): Boolean {
        TODO("Not yet implemented")
    }

    private fun shiftLeft(n: UInt): BigInt2 {
        var result = this

        // TODO improve this to do real shift
        repeat(n.toInt()) {
            result = result * BigInt2(2)
        }

        return result
    }

    // TODO Java version is returning different in sqrtAndRemainder test but this is not necessarily wrong
    private fun shiftRight(n: UInt): BigInt2 {
        val shift = (n % 32u).toInt()
        val drop = n / 32u
        val newArr = arr.drop(drop.toInt()).toMutableList()

        for(i in newArr.indices)
            newArr[i] = newArr[i] shr shift

        // TODO drop zero values
        return BigInt2(sign, newArr.toUIntArray())
    }

    override fun hashCode(): Int {
        var result = sign.toInt()
        result = 31 * result + arr.hashCode()
        return result
    }

    private val bitLength: UInt
        get() {
            return ((arr.size - 1) * 32).toUInt() + arr.last().bitLength()
        }

    override fun add(x: UIntArray, y: UIntArray): UIntArray = BigIntArray.add(x, y)
    override fun subtract(x: UIntArray, y: UIntArray): UIntArray = BigIntArray.subtract(x, y)
    override fun multiply(x: UIntArray, y: UIntArray) = BigIntArray.multiply(x, y)
    override fun multiply(x: UIntArray, y: UInt) = BigIntArray.multiply(x, y)
    override fun pow(x: UIntArray, n: Int) = BigIntArray.pow(x, n)
}

fun BigInteger.toBigInt(): BigInt2 = BigInt2(this.toString())

// Returns larger half as UInt
inline fun ULong.toShiftedUInt(): UInt = (this shr 32).toUInt()
inline fun UInt.bitLength(): UInt = (ln(this.toDouble()) / ln(2.0)).toUInt() + 1u