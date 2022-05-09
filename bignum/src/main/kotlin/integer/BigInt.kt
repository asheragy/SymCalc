package org.cerion.math.bignum.integer

interface BigInt<T : BigInt<T>> : Comparable<T> {
    fun add(other: T): T

    operator fun plus(other: T): T = this.add(other)

    fun sqrtRemainder(): Pair<T, T>

    companion object {
        const val POSITIVE = (1).toByte()
        const val NEGATIVE = (-1).toByte()
        const val ZEROSIGN = 0.toByte()
    }
}