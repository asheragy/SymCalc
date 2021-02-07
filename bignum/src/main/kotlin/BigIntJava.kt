package org.cerion.math.bignum

import java.math.BigDecimal
import java.math.BigInteger

class BigIntJava(private val value: BigInteger) : IBigInt {

    companion object {
        val ZERO = BigIntJava(BigInteger.ZERO)
        val ONE = BigIntJava(BigInteger.ONE)
    }

    override fun toString(): String = value.toString()

    override fun abs(): IBigInt = BigIntJava(value.abs())
    override fun signum(): Int = value.signum()
    override fun testBit(n: Int): Boolean = value.testBit(n)
    override fun toDouble(): Double = value.toDouble()
    override fun toInt(): Int = value.toInt()
    override fun toBigInteger(): BigInteger = value
    override fun toBigDecimal(): BigDecimal = value.toBigDecimal()

    override fun negate(): IBigInt = BigIntJava(value.negate())
    override fun add(other: IBigInt): IBigInt = BigIntJava(value.add(other.toBigInteger()))
    override fun subtract(other: IBigInt): IBigInt = BigIntJava(value.subtract(other.toBigInteger()))
    override fun multiply(other: IBigInt): IBigInt = BigIntJava(value.multiply(other.toBigInteger()))
    override fun divide(other: IBigInt): IBigInt = BigIntJava(value.divide(other.toBigInteger()))
    override fun pow(n: Int): IBigInt = BigIntJava(value.pow(n))

    override fun sqrtRemainder(): Pair<IBigInt, IBigInt> {
        val sqrt = value.sqrtRemainder()
        return Pair(BigIntJava(sqrt.first), BigIntJava(sqrt.second))
    }

    override fun gcd(n: IBigInt): IBigInt = BigIntJava(value.gcd(n.toBigInteger()))
    override fun mod(m: IBigInt): IBigInt = BigIntJava(value.mod(m.toBigInteger()))
    override fun modPow(exponent: IBigInt, m: IBigInt): IBigInt = BigIntJava(value.modPow(exponent.toBigInteger(), m.toBigInteger()))
    override fun isProbablePrime(certainty: Int): Boolean = value.isProbablePrime(certainty)

    override fun divideAndRemainder(other: IBigInt): Pair<IBigInt, IBigInt> {
        val dr = value.divideAndRemainder(other.toBigInteger())
        return Pair(BigIntJava(dr[0]), BigIntJava(dr[1]))
    }

    override fun equals(other: Any?): Boolean {
        if (other is BigIntJava)
            return this.compareTo(other) == 0

        return false
    }

    override fun hashCode(): Int = value.hashCode()
    override fun compareTo(other: IBigInt): Int = value.compareTo(other.toBigInteger())
}