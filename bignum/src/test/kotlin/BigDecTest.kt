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
        assertEquals(BigDec("30.9091"), BigDec("1020").divide(BigDec("33"), 6))

        // Rounding
        assertEquals(BigDec("0.99010"), BigDec("100").divide(BigDec("101"), 5))
        assertEquals(BigDec("0.66667"), BigDec("2").divide(BigDec("3"), 5))
    }

    @Test
    fun divide_sign() {
        assertEquals(BigDec("-0.05"), BigDec("1").divide(BigDec("-20"), 1))
        assertEquals(BigDec("-0.05"), BigDec("-1").divide(BigDec("20"), 1))
        assertEquals(BigDec("0.05"), BigDec("-1").divide(BigDec("-20"), 1))
    }

    @Test
    fun divide_real() {
        assertEquals(BigDec("30.9091"), BigDec("10.20").divide(BigDec("0.33"), 6))
        assertEquals(BigDec("301.818"), BigDec("33.20").divide(BigDec("0.11"), 6))
        assertEquals(BigDec("0.2697368421"), BigDec("0.00123").divide(BigDec("0.00456"), 10))
        assertEquals(BigDec("5000.000005"), BigDec("10000.00001").divide(BigDec("2"), 10))
        assertEquals(BigDec("0.00020"), BigDec("2").divide(BigDec("10000.00001"), 2))
    }

    @Test
    fun pi() {
        //assertEquals(BigDec("3.1415"), BigDec.getPiToDigits(5))
    }

    @Test
    fun toDouble() {
        assertEquals(100.0, BigDec("100").toDouble())
        assertEquals(0.1, BigDec("0.1").toDouble())
    }

    @Test
    fun sqrt() {
        assertEquals(BigDec("100.02499687578100594"), BigDec("10005").sqrt(20))
    }


    @Test
    fun debug() {
        assertEquals(BigDec("5000.000005"), BigDec("10000.00001").divide(BigDec("2"), 10))
    }
}