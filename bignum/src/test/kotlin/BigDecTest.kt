package org.cerion.math.bignum

import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.math.BigDecimal
import kotlin.test.*

@ExperimentalUnsignedTypes
class BigDecTest {

    @Test
    fun toFromString() {
        BigDec("10000000000.0").apply {
            assertEquals(1, scale)
            assertEquals("100000000000", value.toString())
            assertEquals("10000000000.0", toString())
        }

        BigDec("10000000000").apply {
            assertEquals(0, scale)
            assertEquals("10000000000", value.toString())
            assertEquals("10000000000", toString())
        }

        BigDec("0.000001").apply {
            assertEquals(6, scale)
            assertEquals("1", value.toString())
            assertEquals("0.000001", toString())
        }
    }

    @Test
    fun addition() {
        val a = BigDec("10000.00")
        val b = BigDec("100")
        val c = BigDec("0.000001")

        assertEquals(BigDec("20000.00"), a + a)
        assertEquals(BigDec("200"), b + b)
        assertEquals(BigDec("0.000002"), c + c)

        assertEquals(BigDec("10100.00"), a + b)
        assertEquals(BigDec("10100.00"), b + a)
        assertEquals(BigDec("10000.000001"), a + c)
        assertEquals(BigDec("10000.000001"), c + a)
        assertEquals(BigDec("100.000001"), b + c)
        assertEquals(BigDec("100.000001"), c + b)
    }

    @Test
    fun subtraction() {
        val a = BigDec("10000.00")
        val b = BigDec("100")
        val c = BigDec("0.000001")

        assertEquals(BigDec("0.00"), a - a)
        assertEquals(BigDec("0"), b - b)
        assertEquals(BigDec("0.000000"), c - c)

        assertEquals(BigDec("9900.00"), a - b)
        assertEquals(BigDec("-9900.00"), b - a)
        assertEquals(BigDec("9999.999999"), a - c)
        assertEquals(BigDec("-9999.999999"), c - a)
        assertEquals(BigDec("99.999999"), b - c)
        assertEquals(BigDec("-99.999999"), c - b)
    }

    @Test
    fun multiply() {
        val a = BigDec("10000.00")
        val b = BigDec("100")
        val c = BigDec("0.000001")

        assertEquals(BigDec("100000000.0000"), a * a)
        assertEquals(BigDec("10000"), b * b)
        assertEquals(BigDec("0.000000000001"), c * c)

        assertEquals(BigDec("1000000.00"), a * b)
        assertEquals(BigDec("1000000.00"), b * a)
        assertEquals(BigDec("0.01000000"), a * c)
        assertEquals(BigDec("0.01000000"), c * a)
        assertEquals(BigDec("0.000100"), b * c)
        assertEquals(BigDec("0.000100"), c * b)
    }

}