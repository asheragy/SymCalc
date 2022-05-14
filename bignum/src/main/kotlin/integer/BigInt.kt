package org.cerion.math.bignum.integer

interface BigInt<T : BigInt<T>> : Comparable<T> {
    fun add(other: T): T
    fun subtract(other: T): T
    fun multiply(other: T): T
    fun divide(other: T): T

    fun pow(n: Int): T
    fun gcd(n: T): T
    fun mod(m: T): T
    fun modPow(exponent: T, m: T): T
    fun negate(): T
    fun abs(): T
    fun toDouble(): Double
    fun toInt(): Int
    fun signum(): Int
    fun testBit(n: Int): Boolean
    val bitLength: UInt
    override fun equals(other: Any?): Boolean

    // Operators
    operator fun plus(other: T): T = this.add(other)
    operator fun minus(other: T): T = this.subtract(other)
    operator fun times(other: T): T = this.multiply(other)
    operator fun div(other: T): T = this.divide(other)

    fun shiftRight(n: UInt): T
    fun shiftLeft(n: UInt): T

    fun divideAndRemainder(other: T): Pair<T, T>
    fun sqrtRemainder(): Pair<T, T>

    // TODO
    // fun isProbablePrime(certainty: Int): Boolean
}