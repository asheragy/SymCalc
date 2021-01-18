package org.cerion.symcalc.number.primitive

import java.math.BigInteger

interface IBigInt : Comparable<IBigInt> {
    fun signum(): Int
    fun testBit(n: Int): Boolean
    fun toDouble(): Double
    fun toInt(): Int
    fun toBigInteger(): BigInteger // For BigDecimal constructor
    fun negate(): IBigInt
    fun add(other: IBigInt): IBigInt
    fun subtract(other: IBigInt): IBigInt
    fun multiply(other: IBigInt): IBigInt
    fun divide(other: IBigInt): IBigInt
    fun pow(n: Int): IBigInt
    fun sqrtRemainder(): Pair<IBigInt, IBigInt>
    fun gcd(n: IBigInt): IBigInt
    fun mod(m: IBigInt): IBigInt
    fun modPow(exponent: IBigInt, m: IBigInt): IBigInt
    fun isProbablePrime(certainty: Int): Boolean
}