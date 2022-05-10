package org.cerion.math.bignum.integer

interface BigInt<T : BigInt<T>> : Comparable<T> {
    fun add(other: T): T
    fun subtract(other: T): T

    // Operators
    operator fun plus(other: T): T = this.add(other)
    operator fun minus(other: T): T = this.subtract(other)

    fun sqrtRemainder(): Pair<T, T>
}