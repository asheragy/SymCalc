package org.cerion.symcalc.number.primitive

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.jvm.isAccessible

const val LONG_MASK = 0xffffffffL

class BigInt(private val arr: IntArray, private val sign: Int = 1) : IBigInt {

    constructor(n: String) : this(parse(n), parseSign(n))

    override fun toString(): String = toBigInteger().toString()

    override fun toBigDecimal(): BigDecimal = toBigInteger().toBigDecimal()
    override fun toBigInteger(): BigInteger {
        val constructor = BigInteger::class.constructors.toList()[11] // Signum / int[]
        constructor.isAccessible = true

        val copy = arr.copyOf()
        copy.reverse()
        return constructor.call(sign, copy)
    }

    init {
        if (sign != 0) {
            if (arr.isEmpty() || (arr.size == 1 && arr[0] == 0))
                throw IllegalArgumentException("Sign should be zero")
        }
        else if (arr.isNotEmpty() && arr.all { it == 0 })
            throw IllegalArgumentException("Sign should NOT zero")
    }

    // Operators
    operator fun plus(other: BigInt): BigInt = this.add(other)
    operator fun minus(other: BigInt): BigInt = this.subtract(other)
    operator fun minus(other: IBigInt): BigInt = this.subtract(other as BigInt)

    override fun add(other: IBigInt): IBigInt = add(other as BigInt)
    fun add(other: BigInt): BigInt {
        return when {
            sign == 0 -> other
            other.sign == 0 -> this
            sign == other.sign -> BigInt(BigIntArray.add(this.arr, other.arr), sign)

            // Subtract since one side is negative
            else ->
                when (BigIntArray.compare(arr, other.arr)) {
                    -1 -> BigInt(BigIntArray.subtract(other.arr, arr), -1 * sign)
                    1 -> BigInt(BigIntArray.subtract(arr, other.arr), sign)
                else -> ZERO
            }
        }
    }

    override fun subtract(other: IBigInt) = this.subtract(other as BigInt)
    fun subtract(other: BigInt): BigInt {
        if (sign == 1 && other.sign == -1)
            return BigInt(BigIntArray.add(arr, other.arr))
        else if (sign == -1 && other.sign == 1)
            return BigInt(BigIntArray.add(arr, other.arr), -1)
        else if (sign == 0)
            return other.negate()
        else if (other.sign == 0)
            return this

        // Sign is equal but need to subtract
        return when (BigIntArray.compare(arr, other.arr)) {
            -1 -> return BigInt(BigIntArray.subtract(other.arr, arr), -1 * sign)
            1 -> return BigInt(BigIntArray.subtract(arr, other.arr), sign)
            else -> ZERO
        }
    }

    companion object {
        val ZERO = BigInt(IntArray(0), 0)

        private fun parse(n: String): IntArray {
            val big = BigInteger(n)
            return big.getMag()
        }

        private fun parseSign(n: String): Int {
            return when (n[0]) {
                '0' -> 0
                '-' -> -1
                else -> 1
            }
        }

        fun of(vararg n: Int): BigInt = BigInt(n.reversedArray()) // Testing use only
    }

    override fun equals(other: Any?) = other is BigInt && sign == other.sign && arr.contentEquals(other.arr)
    override fun signum() = sign

    override fun testBit(n: Int): Boolean {
        if (n != 0)
            throw Exception() // only supporting to check for odd

        if (sign == 0)
            return false

        return arr[0] % 2 != 0
    }

    override fun toDouble(): Double {
        TODO("Not yet implemented")
    }

    override fun toInt(): Int {
        TODO("Not yet implemented")
    }

    override fun negate() = if (sign == 0) this else BigInt(arr, -1 * sign)
    override fun abs() = if (sign == -1) BigInt(arr, 1) else this

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
            -1 -> BigIntArray.compare(other.arr, arr)
            1 -> BigIntArray.compare(arr, other.arr)
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

