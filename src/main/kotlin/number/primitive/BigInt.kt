package org.cerion.symcalc.number.primitive

import java.lang.ArithmeticException
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.jvm.isAccessible

@ExperimentalUnsignedTypes
class BigInt constructor(private val sign: Byte, private val arr: UIntArray) : IBigInt {

    constructor(s: Int, arr: UIntArray) : this (s.toByte(), arr)
    constructor(n: String) : this(parseSign(n), parse(n))

    override fun toString(): String = toBigInteger().toString()

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

    companion object {
        val ZERO = BigInt(0, UIntArray(0))
        private val POSITIVE = (1).toByte()
        private val NEGATIVE = (-1).toByte()
        private val ZEROSIGN = 0.toByte()

        private fun parse(n: String): UIntArray {
            val big = BigInteger(n)
            return big.getMag().map { it.toUInt() }.toUIntArray()
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

    override fun multiply(other: IBigInt): IBigInt {
        TODO("Not yet implemented")
    }

    override fun divide(other: IBigInt): IBigInt {
        TODO("Not yet implemented")
    }

    override fun pow(n: Int): IBigInt {
        TODO("Not yet implemented")
    }

    override fun sqrtRemainder(): Pair<IBigInt, IBigInt> {
        TODO("Not yet implemented")
    }

    override fun gcd(n: IBigInt): IBigInt {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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
}

private fun BigInteger.getMag(): IntArray {
    return javaClass.getDeclaredField("mag").let {
        it.isAccessible = true
        val value = it.get(this) as IntArray
        value.reverse()
        return@let value
    }
}

fun BigInteger.toBigInt(): BigInt = BigInt(this.toString())

