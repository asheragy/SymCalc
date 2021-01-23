package org.cerion.symcalc.number.primitive

import org.junit.Test
import java.lang.ArithmeticException
import kotlin.test.*


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

        // Negative
        assertEquals(BigInt.of(2,2).negate(), BigInt.of(1,1) - BigInt.of(3,3))
    }

    @Test
    fun additionSigned() {
        assertEquals(BigInt("10000000000"), BigInt("21111111111") + BigInt("-11111111111"))
        assertEquals(BigInt("-20000000000"), BigInt("21111111111") + BigInt("-41111111111"))

        assertEquals(BigInt("-10000000000"), BigInt("-21111111111") + BigInt("11111111111"))
        assertEquals(BigInt("30000000000"), BigInt("-21111111111") + BigInt("51111111111"))

        assertEquals(BigInt("-72222222222"), BigInt("-21111111111") + BigInt("-51111111111"))
    }

    @Test
    fun subtractionSigned() {
        // One negative (internal addition)
        assertEquals(BigInt("32222222222"), BigInt("21111111111") - BigInt("-11111111111"))
        assertEquals(BigInt("-32222222222"), BigInt("-21111111111") - BigInt("11111111111"))

        // Both negative
        assertEquals(BigInt("-10000000000"), BigInt("-21111111111") - BigInt("-11111111111"))
        assertEquals(BigInt("30000000000"), BigInt("-21111111111") - BigInt("-51111111111"))
    }

    @Test
    fun compare() {
        assertTrue(BigInt.of(1,2) > BigInt.of(9))
        assertTrue(BigInt.of(9) < BigInt.of(1,0))

        // Negative is opposite
        assertTrue(BigInt.of(1,2).negate() < BigInt.of(9))
        assertTrue(BigInt.of(9) > BigInt.of(1,0).negate())

        // Equal
        assertEquals(BigInt.of(1,1), BigInt.of(1,1))

        // Not Equal
        assertNotEquals(BigInt.of(1,1), BigInt.of(1,1).negate())
        assertNotEquals(BigInt.of(1,1), BigInt.of(1,1,1))
        assertNotEquals(BigInt.of(1,1), BigInt.of(1,0))
    }

    @Test
    fun abs() {
        assertEquals(BigInt("11"), BigInt("11").abs())
        assertEquals(BigInt("11"), BigInt("-11").abs())
        assertEquals(BigInt("0"), BigInt("0").abs())
    }

    @Test
    fun testBit() {
        assertFalse(BigInt("123456789022").testBit(0))
        assertFalse(BigInt("-2").testBit(0))
        assertFalse(BigInt("0").testBit(0))

        assertTrue(BigInt("12345678901").testBit(0))
        assertTrue(BigInt("1").testBit(0))
        assertTrue(BigInt("-1").testBit(0))
    }

    @Test
    fun toNumber() {
        assertEquals(0.0, BigInt("0").toDouble())
        assertEquals(0, BigInt("0").toInt())

        assertEquals(1234567890.0, BigInt("1234567890").toDouble())
        assertEquals(1234567890, BigInt("1234567890").toInt())

        assertEquals(-1234567890.0, BigInt("-1234567890").toDouble())
        assertEquals(-1234567890, BigInt("-1234567890").toInt())
    }

    @Test
    fun toNumberInvalid() {
        val invalid = listOf("3000000000", "-3000000000")
        invalid.forEach {
            assertFailsWith(ArithmeticException::class) { BigInt(it).toDouble() }
            assertFailsWith(ArithmeticException::class) { BigInt(it).toInt() }
        }
    }
}