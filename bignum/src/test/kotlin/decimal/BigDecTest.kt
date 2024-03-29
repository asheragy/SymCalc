package org.cerion.math.bignum.decimal

import org.junit.Test
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
    fun fromDouble() {
        // TODO bunch of issues with this on big and small numbers
        assertEquals(BigDec("10"), BigDec(10.0))
        assertEquals(BigDec("0"), BigDec(0.0))
        assertEquals(BigDec("0.1"), BigDec(0.1))
        assertEquals(BigDec("0.0000001"), BigDec(0.0000001))
        assertEquals(BigDec("10000.000000100002"), BigDec(10000.0000001))
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
    fun multiply_precision() {
        assertEquals(BigDec("15241.383936"), BigDec("123.456").multiply(BigDec("123.456"), MathContext(11, RoundingMode.HALF_UP)))
        assertEquals(BigDec("15241.38"), BigDec("123.456").multiply(BigDec("123.456"), MathContext(7, RoundingMode.HALF_UP)))
        assertEquals(BigDec("15241.384"), BigDec("123.456").multiply(BigDec("123.456"), MathContext(8, RoundingMode.HALF_UP)))
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
        val pi100 = "3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679"

        // TODO other algorithm seems to be better on precision
        for(i in 2 until 12) {
            val computed = BigDec.getPiToDigits(i).toString()
            assertEquals(pi100.substring(0, computed.length-2), computed.substring(0, computed.length - 2))
        }
    }

    @Test
    fun toDouble() {
        assertEquals(100.0, BigDec("100").toDouble())
        assertEquals(0.0, BigDec("0").toDouble())
        assertEquals(0.1, BigDec("0.1").toDouble())
        assertEquals(0.01, BigDec("0.01").toDouble())
        assertEquals(12345.6789, BigDec("12345.67890").toDouble())
        assertEquals(1.0E-36, BigDec("0.000000000000000000000000000000000001").toDouble())
    }

    @Test
    fun sqrt() {
        assertEquals(BigDec("0.3162277660"), BigDec("0.1").sqrt(10))
        assertEquals(BigDec("100.02499687578100594"), BigDec("10005").sqrt(20))
    }

    @Test
    fun pow() {
        assertEquals(BigDec("2.5937424601"), BigDec("1.1").pow(10))
        assertEquals(BigDec("2.59374"), BigDec("1.1").pow(10, MathContext(6, RoundingMode.HALF_UP)))
        // Extra precision needed in calculation for this to work
        //assertEquals(BigDec("2.594"), BigDec("1.1").pow(10, MathContext(4, RoundingMode.HALF_UP)))
    }


    @Test
    fun debug() {
        assertEquals(BigDec("5000.000005"), BigDec("10000.00001").divide(BigDec("2"), 10))
    }
}