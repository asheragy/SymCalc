package org.cerion.symcalc.number.primitive

import org.junit.Test
import kotlin.test.assertEquals


internal class BigIntTest {

    @Test
    fun addition() {
        assertEquals(BigInt.of(1,0), BigInt.of(-1) + BigInt.of(1))

        // Digit carried
        assertEquals(BigInt.of(1,0,0), BigInt.of(-1,-1) + BigInt.of(1))
        assertEquals(BigInt.of(1,2,0), BigInt.of(1,1,-1) + BigInt.of(1))

        // Larger second
        assertEquals(BigInt.of(1,1,1,2), BigInt.of(1) + BigInt.of(1,1,1,1))
    }

    @Test
    fun subtract() {
        assertEquals(BigInt.of(2,2), BigInt.of(3,3) - BigInt.of(1,1))

        // Digits borrowed
        assertEquals(BigInt.of(4,-1), BigInt.of(5,1) - BigInt.of(2))
        assertEquals(BigInt.of(3,-4), BigInt.of(5,1) - BigInt.of(1,5))
        assertEquals(BigInt.of(1,-2,-1), BigInt.of(2,0,1) - BigInt.of(1,2))
        assertEquals(BigInt.of(1,3,-4), BigInt.of(1,5,1) - BigInt.of(1,5))

        // Result shrinks in size
        assertEquals(BigInt.of(1), BigInt.of(1,1,1,2) - BigInt.of(1,1,1,1))
    }

    @Test
    fun additionSubtractionSigned() {

    }
}