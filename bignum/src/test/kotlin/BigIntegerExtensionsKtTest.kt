package org.cerion.math.bignum

import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.math.BigInteger

internal class BigIntegerExtensionsKtTest {

    @Test
    fun sqrtAndRemainder() {
        var sqrt = BigInteger("10000000000000000000009").sqrtRemainder()
        assertEquals(BigInteger("100000000000"), sqrt.first)
        assertEquals(BigInteger("9"), sqrt.second)

        // Initial value is not determined by converting to double
        sqrt = BigInteger("10000000000000000000000000000000000000009").sqrtRemainder()
        assertEquals(BigInteger("100000000000000000000"), sqrt.first)
        assertEquals(BigInteger("9"), sqrt.second)
    }

    @Test
    fun nthRootAndRemainder() {
        var root = BigInteger("1000000000000000000009").nthRootAndRemainder(3)
        assertEquals(BigInteger("10000000"), root.first)
        assertEquals(BigInteger("9"), root.second)

        root = BigInteger("16935003133160595268336552").nthRootAndRemainder(5)
        assertEquals(BigInteger("111111"), root.first)
        assertEquals(BigInteger("1"), root.second)

    }
}