package org.cerion.symcalc.number.primitive

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.*
import kotlin.reflect.jvm.isAccessible


@ExperimentalUnsignedTypes
class BigInt : IBigInt {
    private val sign: Byte
    private val arr: UIntArray

    companion object {
        val ZERO = BigInt(0, UIntArray(0))
        val ONE = BigInt(1, UIntArray(1) { 1u })
        val NEGATIVE_ONE = BigInt(-1, UIntArray(1) { 1u })
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

    constructor(n: String) {
        sign = when (n[0]) {
            '0' -> 0
            '-' -> -1
            else -> 1
        }

        val offset = if(n[0] == '-') 1 else 0
        val firstDigits = n.subSequence(offset, offset + if ((n.length-offset) % 9 == 0) 9 else (n.length-offset) % 9).toString()
        val remainingDigits = n.substring(firstDigits.length + offset).chunked(9).map { Integer.parseInt(it).toUInt() }

        if (firstDigits == "0")
            this.arr = UIntArray(0)
        else {
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
    operator fun plus(other: BigInt): BigInt = this.add(other)
    operator fun minus(other: BigInt): BigInt = this.subtract(other)
    operator fun minus(other: IBigInt): BigInt = this.subtract(other as BigInt)
    operator fun times(other: BigInt): BigInt = this.multiply(other)
    operator fun times(other: IBigInt): BigInt = this.multiply(other as BigInt)
    operator fun div(other: BigInt): BigInt = this.divide(other)
    operator fun div(other: IBigInt): BigInt = this.divide(other as BigInt)

    override fun add(other: IBigInt): IBigInt = add(other as BigInt)
    fun add(other: BigInt): BigInt {
        return when {
            sign == ZEROSIGN -> other
            other.sign == ZEROSIGN -> this
            sign == other.sign -> BigInt(sign, BigIntArray.add(this.arr, other.arr))

            // Subtract since one side is negative
            else ->
                when (BigIntArray.compare(arr, other.arr)) {
                    -1 -> BigInt(-1 * sign, BigIntArray.subtract(other.arr, arr))
                    1 -> BigInt(sign, BigIntArray.subtract(arr, other.arr))
                else -> ZERO
            }
        }
    }

    override fun subtract(other: IBigInt) = this.subtract(other as BigInt)
    fun subtract(other: BigInt): BigInt {
        if (sign == POSITIVE && other.sign == NEGATIVE)
            return BigInt(1, BigIntArray.add(arr, other.arr))
        else if (sign == NEGATIVE && other.sign == POSITIVE)
            return BigInt(-1, BigIntArray.add(arr, other.arr))
        else if (sign == ZEROSIGN)
            return other.negate()
        else if (other.sign == ZEROSIGN)
            return this

        // Sign is equal but need to subtract
        return when (BigIntArray.compare(arr, other.arr)) {
            -1 -> return BigInt(-1 * sign, BigIntArray.subtract(other.arr, arr))
            1 -> return BigInt(sign, BigIntArray.subtract(arr, other.arr))
            else -> ZERO
        }
    }

    override fun multiply(other: IBigInt) = this.multiply(other as BigInt)
    fun multiply(other: BigInt): BigInt {
        if (sign == ZEROSIGN || other.sign == ZEROSIGN)
            return ZERO

        return BigInt(if(sign == other.sign) 1 else -1, BigIntArray.multiply(arr, other.arr))
    }

    override fun divide(other: IBigInt) = this.divide(other as BigInt)
    fun divide(other: BigInt): BigInt = divideAndRemainder(other).first as BigInt

    fun divide(other: UInt): BigInt {
        return BigInt(1, BigIntArray.divide(this.arr,other))
    }

    override fun equals(other: Any?) = other is BigInt && sign == other.sign && arr.contentEquals(other.arr)
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

    override fun negate() = if (sign == ZEROSIGN) this else BigInt(-1 * sign, arr)
    override fun abs() = if (sign == NEGATIVE) BigInt(1, arr) else this

    override fun pow(n: Int): IBigInt {
        return when(n.sign) {
            -1 -> throw UnsupportedOperationException("exponent cannot be negative")
            0 -> ONE
            else -> {
                if (n == 1 || this == ONE)
                    return this

                if (this == NEGATIVE_ONE)
                    return if(n % 2 == 0) this.negate() else this

                val resultSign = if(sign == (-1).toByte() && n % 2 == 1) -1 else 1
                return BigInt(resultSign.toByte(), BigIntArray.pow(arr, n))
            }
        }
    }

    override fun gcd(n: IBigInt): IBigInt {
        n as BigInt
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

        return BigInt(1, b)
    }

    override fun mod(m: IBigInt): IBigInt {
        m as BigInt
        val rem = this.divideAndRemainder(m).second as BigInt

        if (rem.sign == NEGATIVE)
            return m + rem

        return rem
    }

    override fun modPow(exponent: IBigInt, m: IBigInt): IBigInt {
        exponent as BigInt
        m as BigInt

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
                    result = b as BigInt
                }

                square = (square * square).mod(m) as BigInt
                n /= 2u
            }
        }

        return result
    }

    override fun divideAndRemainder(other: IBigInt): Pair<IBigInt, IBigInt> {
        //println("$this / $other")
        other as BigInt
        val div = BigIntArray.divide(this.arr, other.arr)

        var sign = (sign * other.sign)
        val rem = when {
            div.second == null -> ZERO
            sign == -1 -> BigInt(NEGATIVE, div.second!!)
            else -> BigInt(POSITIVE, div.second!!)
        }

        if (div.first.isEmpty())
            sign = 0

        return Pair(BigInt(sign, div.first), rem)
    }

    override fun sqrtRemainder(): Pair<IBigInt, IBigInt> {
        if (sign == NEGATIVE)
            throw ArithmeticException()

        // Newton method, get initial estimate
        val bl = bitLength
        var x = if (bl > 120u)
            shiftRight(bl / 2u - 1u)
        else
            BigInt("" + sqrt(toDouble()).roundToLong())

        val two = BigInt(2)
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

    override fun compareTo(other: IBigInt): Int {
        other as BigInt

        if (sign != other.sign)
            return sign.compareTo(other.sign)

        return when(sign) {
            NEGATIVE -> BigIntArray.compare(other.arr, arr)
            POSITIVE -> BigIntArray.compare(arr, other.arr)
            else -> 0
        }
    }

    override fun toString(): String {
        if (sign == ZEROSIGN)
            return "0"

        val sb = StringBuilder()
        if (sign == NEGATIVE)
            sb.append("-")

        val base = 65536
        val maxDigit = 1000000000uL
        val result = mutableListOf<UInt>()

        result.add((arr.last() % maxDigit).toUInt())
        if (arr.last() / maxDigit != 0uL)
            result.add((arr.last() / maxDigit).toUInt())

        for(index in arr.size - 2 downTo 0) {
            for (i in 0..1) {
                // Multiply entire result array by base
                var temp: ULong = 0uL
                for (j in result.indices) {
                    temp = result[j] * base.toULong() + (temp / maxDigit)
                    result[j] = (temp % maxDigit).toUInt()
                }

                // Multiplication carry
                if (temp / maxDigit != 0uL)
                    result.add((temp / maxDigit).toUInt())

                // Add current digit
                temp = result[0].toULong() + if (i == 1) arr[index].toUShort() else (arr[index] shr 16).toUShort()
                result[0] = (temp % maxDigit).toUInt()

                while(temp /maxDigit != 0uL) {
                    var index = 1;
                    if (index == result.size) {
                        result.add(1u) // TODO see if any test case fits here, no coverage yet
                        break
                    }
                    else {
                        temp = result[index].toULong() + (temp / maxDigit)
                        result[index++] = (temp % maxDigit).toUInt()
                    }
                }
            }
        }

        sb.append(result.last())
        for(i in result.size - 2 downTo 0) {
            sb.append("0".repeat(9 - result[i].toString().length))
            sb.append(result[i])
        }

        return sb.toString()
    }

    override fun isProbablePrime(certainty: Int): Boolean {
        TODO("Not yet implemented")
    }

    private fun shiftLeft(n: UInt): BigInt {
        var result = this

        // TODO improve this to do real shift
        repeat(n.toInt()) {
            result = result * BigInt(2)
        }

        return result
    }

    // TODO Java version is returning different in sqrtAndRemainder test but this is not necessarily wrong
    private fun shiftRight(n: UInt): BigInt {
        val shift = (n % 32u).toInt()
        val drop = n / 32u
        val newArr = arr.drop(drop.toInt()).toMutableList()

        for(i in newArr.indices)
            newArr[i] = newArr[i] shr shift

        // TODO drop zero values
        return BigInt(sign, newArr.toUIntArray())
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
}

fun BigInteger.toBigInt(): BigInt = BigInt(this.toString())

// Returns larger half as UInt
inline fun ULong.toShiftedUInt(): UInt = (this shr 32).toUInt()
inline fun UInt.bitLength(): UInt = (ln(this.toDouble()) / ln(2.0)).toUInt() + 1u