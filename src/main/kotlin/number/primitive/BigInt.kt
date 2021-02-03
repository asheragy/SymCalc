package org.cerion.symcalc.number.primitive

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.jvm.isAccessible


@ExperimentalUnsignedTypes
class BigInt constructor(private val sign: Byte, private val arr: UIntArray) : IBigInt {

    constructor(s: Int, arr: UIntArray) : this (s.toByte(), arr)
    constructor(n: String) : this(parseSign(n), parse(n))

    override fun toBigDecimal(): BigDecimal = toBigInteger().toBigDecimal()
    override fun toBigInteger(): BigInteger {
        val constructor = BigInteger::class.constructors.toList()[11] // Signum / int[]
        constructor.isAccessible = true

        val copy = arr.map { it.toInt() }.reversed().toIntArray()
        return constructor.call(sign, copy)
    }

    init {
        // Temp error checking
        if (sign != ZEROSIGN) {
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

    companion object {
        val ZERO = BigInt(0, UIntArray(0))
        val ONE = BigInt(1, UIntArray(1) { 1u })
        private val POSITIVE = (1).toByte()
        private val NEGATIVE = (-1).toByte()
        private val ZEROSIGN = 0.toByte()

        private fun parse(n: String): UIntArray {
            val offset = if(n[0] == '-') 1 else 0
            val firstDigits = n.subSequence(offset, offset + if ((n.length-offset) % 9 == 0) 9 else (n.length-offset) % 9).toString()
            val remainingDigits = n.substring(firstDigits.length + offset).chunked(9).map { Integer.parseInt(it).toUInt() }

            if (firstDigits == "0")
                return UIntArray(0)

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

            return result.toUIntArray()
        }

        private fun parseSign(n: String): Int {
            return when (n[0]) {
                '0' -> 0
                '-' -> -1
                else -> 1
            }
        }
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
            else -> throw ArithmeticException()
        }
    }

    override fun toInt(): Int {
        return when (arr.size) {
            0 -> 0
            1 -> {
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
        TODO("Not yet implemented")
    }

    override fun sqrtRemainder(): Pair<IBigInt, IBigInt> {
        TODO("Not yet implemented")
    }

    override fun gcd(n: IBigInt): IBigInt {
        n as BigInt

        // A is the larger of the two numbers
        var a: UIntArray
        var b: UIntArray
        when(BigIntArray.compare(arr, n.arr)) {
            1 -> { a = arr; b = n.arr }
            -1 -> { b = arr; a = n.arr }
            else -> return this
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
        TODO("Not yet implemented")
    }

    override fun modPow(exponent: IBigInt, m: IBigInt): IBigInt {
        TODO("Not yet implemented")
    }

    override fun isProbablePrime(certainty: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun divideAndRemainder(other: IBigInt): Pair<IBigInt, IBigInt> {
        //println("$this / $other")
        other as BigInt
        val div = BigIntArray.divide(this.arr, other.arr)
        return Pair(BigInt(1, div.first), if (div.second != null) BigInt(1, div.second!!) else ZERO)
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

    // Debug
    fun printDigits() {
        arr.forEach {
            print("$it ")
        }
        println()
    }
}

fun BigInteger.toBigInt(): BigInt = BigInt(this.toString())

// Returns larger half as UInt
inline fun ULong.toShiftedUInt(): UInt = (this shr 32).toUInt()
