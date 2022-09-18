package org.cerion.math.bignum.integer

import java.math.BigInteger


class BinomialGenerator(maxN: Int) {
    private val values = Array(maxN+1) { BigInteger.ONE }
    private var position = 0

    fun inc() {
        for(i in position downTo 1)
            values[i] += values[i-1]

        position++
    }

    operator fun get(index: Int): BigInteger {
        if (index > position)
            throw IndexOutOfBoundsException()

        return values[index]
    }

}