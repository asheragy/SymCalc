package org.cerion.symcalc.number.primitive

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.jvm.isAccessible

const val LONG_MASK = 0xffffffffL

class BigInt(private val arr: IntArray) : IBigInt {

    constructor(n: String) : this(parse(n))

    override fun toString(): String = toBigInteger().toString()
    override fun toBigInteger(): BigInteger {
        val constructor = BigInteger::class.constructors.toList()[11] // Signum / int[]
        constructor.isAccessible = true

        val copy = arr.copyOf()
        copy.reverse()
        return constructor.call(1, copy)
    }

    override fun add(other: IBigInt): IBigInt = add(other as BigInt)

    fun add(other: BigInt) = BigInt(add(this.arr, other.arr))

    companion object {
        private fun parse(n: String): IntArray {
            val big = BigInteger(n)
            return big.getMag()
        }
    }

    override fun equals(other: Any?): Boolean {
        TODO("Not yet implemented")
    }

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
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }
}

private fun BigInteger.getMag(): IntArray {
    return javaClass.getDeclaredField("mag").let {
        it.isAccessible = true
        val value = it.get(this) as IntArray
        value.reverse()
        return@let value;
    }
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