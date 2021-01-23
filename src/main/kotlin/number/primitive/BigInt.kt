package org.cerion.symcalc.number.primitive

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.jvm.isAccessible

const val LONG_MASK = 0xffffffffL

class BigInt(private val arr: IntArray, private val sign: Int = 1) : IBigInt {

    constructor(n: String) : this(parse(n), if(n[0] == '-') -1 else 1)

    override fun toString(): String = toBigInteger().toString()
    override fun toBigInteger(): BigInteger {
        val constructor = BigInteger::class.constructors.toList()[11] // Signum / int[]
        constructor.isAccessible = true

        val copy = arr.copyOf()
        copy.reverse()
        return constructor.call(sign, copy)
    }

    // Operators
    operator fun plus(other: BigInt): BigInt = this.add(other)
    operator fun minus(other: BigInt): BigInt = this.subtract(other)
    operator fun minus(other: IBigInt): BigInt = this.subtract(other as BigInt)

    override fun add(other: IBigInt): IBigInt = add(other as BigInt)

    fun add(other: BigInt): BigInt {
        return when (other.sign) {
            -1 -> this.subtract(other.negate() as BigInt) // TODO fix
            1 -> {
                if (sign == -1)
                    return other - this.negate()

                BigInt(add(this.arr, other.arr))
            }
            else -> this
        }
    }

    fun subtract(other: BigInt): BigInt {
        if (sign == 1 && other.sign == -1)
            return BigInt(add(arr, other.arr))
        else if (sign == -1 && other.sign == 1)
            return BigInt(add(arr, other.arr), -1)
        else if (sign == 0)
            return other.negate() as BigInt
        else if (other.sign == 0)
            return this

        // Sign is equal but need to subtract
        return when (compare(arr, other.arr)) {
            -1 -> return BigInt(subtract(other.arr, arr), -1 * sign)
            1 -> return BigInt(subtract(arr, other.arr), sign)
            else -> ZERO
        }
    }

    companion object {
        val ZERO = BigInt(IntArray(0), 0)

        private fun parse(n: String): IntArray {
            val big = BigInteger(n)
            return big.getMag()
        }

        fun of(vararg n: Int): BigInt = BigInt(n.reversedArray()) // Testing use only
    }

    override fun equals(other: Any?) = other is BigInt && sign == other.sign && arr.contentEquals(other.arr)

    override fun abs(): IBigInt {
        TODO("Not yet implemented")
    }

    override fun signum(): Int {
        TODO("Not yet implemented")
    }

    override fun testBit(n: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun toDouble(): Double {
        TODO("Not yet implemented")
    }

    override fun toInt(): Int {
        TODO("Not yet implemented")
    }

    override fun toBigDecimal(): BigDecimal {
        TODO("Not yet implemented")
    }

    override fun negate(): IBigInt {
        if (sign == 0)
            return this

        return BigInt(arr, -1 * sign)
    }

    override fun subtract(other: IBigInt): IBigInt {
        TODO("Not yet implemented")
    }

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
            -1 -> compare(other.arr, arr)
            1 -> compare(arr, other.arr)
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

private fun compare(x: IntArray, y: IntArray): Int {
    val size = x.size.compareTo(y.size)
    if (size != 0)
        return size

    // Start at most significant digit
    for (i in x.size - 1 downTo 0)
        if (x[i].compareTo(y[i]) != 0)
            return x[i].compareTo(y[i])

    return 0
}

private fun add(x: IntArray, y: IntArray): IntArray {
    val a = if (x.size >= y.size) x else y
    val b = if (a === x) y else x

    val arr = IntArray(a.size)
    var index = 0
    var sum: Long = 0

    // Add digits while array lengths are the same
    while(index < b.size) {
        sum = (a[index].toLong() and LONG_MASK) + (b[index].toLong() and LONG_MASK) + (sum ushr 32)
        arr[index++] = sum.toInt()
    }

    // Add digits for larger number while digit is still carried
    var carry = sum ushr 32 != 0L
    while(index < a.size && carry) {
        sum = (a[index].toLong() and LONG_MASK) + (sum ushr 32)
        carry = sum ushr 32 != 0L
        arr[index++] = sum.toInt()
    }

    // If there is still a digit to carry add it and return
    if (carry) {
        val t = arr.copyOf(arr.size + 1)
        t[arr.size] = 1
        return t
    }

    // If no more carry just add the remaining digits
    while(index < a.size)
        arr[index] = a[index++]

    return arr
}

private fun subtract(x: IntArray, y: IntArray): IntArray {
    // Input is always expected to have x as the largest array and adjust result sign accordingly
    val arr = IntArray(x.size) // TODO is there a benefit in copying starting array? need to performance test first
    var index = 0
    var diff = 0L

    while(index < y.size) {
        diff = (x[index].toLong() and LONG_MASK) - (y[index].toLong() and LONG_MASK) + (diff shr 32)
        arr[index++] = diff.toInt()
    }

    var borrow = (diff shr 32) != 0L
    while(index < x.size && borrow) {
        diff = (x[index].toLong() and LONG_MASK) - 1
        borrow = diff shr 32 != 0L
        arr[index++] = diff.toInt()
    }

    while(index < x.size)
        arr[index] = x[index++]

    return removeLeadingZeros(arr)
}

private fun removeLeadingZeros(arr: IntArray): IntArray {
    var digits = 0
    for(i in arr.size -1 downTo 0) {
        if (arr[i] == 0)
            digits++
        else
            break
    }

    if (digits > 0) // TODO, this drops elements from end of array is there a way to do that without copy?
        return arr.copyOf(arr.size - digits)

    return arr
}