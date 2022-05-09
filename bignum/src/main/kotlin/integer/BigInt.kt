package org.cerion.math.bignum.integer

interface BigInt<T : BigInt<T>> : Comparable<T> {
    fun add(other: T): T

    operator fun plus(other: T): T = this.add(other)

    fun sqrtRemainder(): Pair<T, T>
}