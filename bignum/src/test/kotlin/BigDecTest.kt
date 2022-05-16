package org.cerion.math.bignum

import org.junit.Test
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.test.assertEquals

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

    @Test
    fun divide() {
        val mc = MathContext(10, RoundingMode.HALF_UP)
        val test = BigDecimal("1000000000.000").divide(BigDecimal("33333.333"), mc)
        val test2 = BigDecimal("1000000000.000").divide(BigDecimal("33333.333"), 10, RoundingMode.HALF_UP)


        val a = BigDecimal("10000.00")
        val b = BigDecimal("200")
        val c = BigDecimal("0.000004")
        val aa = BigDec("10000.00")
        val bb = BigDec("200")
        val cc = BigDec("0.000004")

        assertEquals(BigDec("1.00"), aa / aa)
        assertEquals(BigDec("1"), bb / bb)
        assertEquals(BigDec("1.000000"), cc / cc)

        //assertEquals(BigDecimal("50.00"), a / b)
        assertEquals(BigDec("50.00"), aa / bb)
        //assertEquals(BigDecimal("0.02"), b.divide(a, MathContext(10, RoundingMode.HALF_UP)))
        //assertEquals(BigDecimal("2500000000.00"), a / c)
        assertEquals(BigDecimal("0.0000000004"), c.divide(a, MathContext(10, RoundingMode.HALF_UP)))
        assertEquals(BigDecimal("50000000"), b / c)
        assertEquals(BigDecimal("0.00000002"), c.divide(b, MathContext(10, RoundingMode.HALF_UP)))
    }

    @Test
    fun divide_wholeNumbers() {
        // Exact
        assertEquals(BigDec("0.50000"), BigDec("1").divide(BigDec("2"), 5))
        assertEquals(BigDec("0.050000"), BigDec("1").divide(BigDec("20"), 5))
        assertEquals(BigDec("1.5000"), BigDec("3").divide(BigDec("2"), 5))
        assertEquals(BigDec("15.000"), BigDec("30").divide(BigDec("2"), 5))

        // Truncated
        assertEquals(BigDec("0.33333"), BigDec("1").divide(BigDec("3"), 5))
        assertEquals(BigDec("0.3333333333333"), BigDec("1").divide(BigDec("3"), 13))
        assertEquals(BigDec("0.030303"), BigDec("1").divide(BigDec("33"), 5))

        // Rounding
        assertEquals(BigDec("0.99010"), BigDec("100").divide(BigDec("101"), 5))
        assertEquals(BigDec("0.66667"), BigDec("2").divide(BigDec("3"), 5))
    }

    @Test
    fun debug() {
        assertEquals(BigDec("1.5000"), BigDec("3").divide(BigDec("2"), 5))
    }
}