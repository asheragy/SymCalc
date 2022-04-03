package org.cerion.math.bignum

import java.math.BigInteger
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

// Custom version of Java 9+ sqrtAndRemainder
fun BigInteger.sqrtRemainder(): Pair<BigInteger, BigInteger> {
    if (this < BigInteger.ZERO)
        throw ArithmeticException()

    // Newton method, get initial estimate
    val bl = bitLength()
    var x = if (bl > 120)
        shiftRight(bl / 2 - 1)
    else
        BigInteger("" + sqrt(toDouble()).roundToLong())

    val two = BigInteger.valueOf(2L)
    while (true) {
        val x2 = x.pow(2)
        var xplus2 = x.add(BigInteger.ONE).pow(2)

        if (x2 <= this && xplus2 > this)
            break

        xplus2 = xplus2.subtract(x.shiftLeft(2))
        if (xplus2 <= this && x2 > this) {
            x = x.subtract(BigInteger.ONE)
            break
        }

        xplus2 = x2.subtract(this).divide(x).divide(two)
        x = x.subtract(xplus2)
    }

    val remainder = this - (x * x)
    return Pair(x, remainder)
}

fun BigInteger.nthRootAndRemainder(n: Int): Pair<BigInteger, BigInteger> {
    if (this < BigInteger.ZERO)
        throw ArithmeticException()

    var x: BigInteger
    val nBigInt = BigInteger.valueOf(n.toLong())
    val bl = bitLength()
    x = if (bl > 120)
        this.shiftRight(bl / n - 1)
    else
        BigInteger("" + toDouble().pow(1.0 / n).roundToLong())

    while (true) {
        val x2 = x.pow(n)
        var xplus2 = x.add(BigInteger.ONE).pow(n)
        if (x2 <= this && xplus2 > this)
            break

        xplus2 = x.subtract(BigInteger.ONE).pow(n)
        if (xplus2 <= this && x2 > this) {
            x = x.subtract(BigInteger.ONE)
            break
        }

        xplus2 = x2.subtract(this).divide(x.pow(n - 1)).divide(nBigInt)
        x = x.subtract(xplus2)
    }

    val remainder = this - x.pow(n)
    return Pair(x, remainder)
}

fun factorial(N: Int): BigInteger {
    if (N < 0)
        throw ArithmeticException("Factorial must be positive integer")

    var result = BigInteger.ONE
    var n = N.toLong()

    while (n > 1) {
        result *= BigInteger.valueOf(n)
        n--
    }

    return result
}

fun binomial(n: Int, k: Int): BigInteger {
    // n! / k!(n-k)!
    val nfact = factorial(n)
    val n2 = factorial(n - k)
    val n3 = factorial(k)

    return nfact / (n2 * n3)
}

// When k is omitted, calculate list of all values of k
fun binomial(n: Int): List<BigInteger> {
    val nfact = factorial(n)

    val result = mutableListOf(BigInteger.ONE)
    var k_fact = BigInteger.ONE
    var nk_fact = nfact

    for (k in 1..n / 2) {
        k_fact *= BigInteger.valueOf(k.toLong())
        nk_fact = (nk_fact / BigInteger.valueOf(n - k + 1L))

        val t = (nfact / (k_fact * nk_fact))
        result.add(t)
    }

    for(k in (n / 2) + 1..n) {
        result.add(result[n - k])
    }

    return result
}