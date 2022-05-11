package org.cerion.math.bignum.integer

import java.math.BigDecimal
import java.math.BigInteger

@Deprecated("unused")
interface IBigInt {
    override fun equals(other: Any?): Boolean
    fun abs(): IBigInt
    fun signum(): Int
    fun testBit(n: Int): Boolean
    fun toDouble(): Double
    fun toInt(): Int
    fun toBigInteger(): BigInteger // For BigDecimal constructor
    fun toBigDecimal(): BigDecimal
    //fun negate(): IBigInt
    //fun add(other: IBigInt): IBigInt
    //fun subtract(other: IBigInt): IBigInt
    //fun multiply(other: IBigInt): IBigInt
    fun divide(other: IBigInt): IBigInt
    //fun pow(n: Int): IBigInt
    //fun sqrtRemainder(): Pair<IBigInt, IBigInt>
    fun gcd(n: IBigInt): IBigInt
    fun mod(m: IBigInt): IBigInt
    fun modPow(exponent: IBigInt, m: IBigInt): IBigInt
    fun isProbablePrime(certainty: Int): Boolean
    fun divideAndRemainder(other: IBigInt): Pair<IBigInt, IBigInt>
}