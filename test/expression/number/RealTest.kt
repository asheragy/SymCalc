package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.core.N
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class RealTest : NumberTestBase() {

    private val RealDouble = RealDouble(1.12345)
    private val RealBigDec = RealBigDec("1.1234567890987654321")

    @Test
    fun addition() {
        assertAdd(RealDouble(2.2469067890987655), RealDouble, RealBigDec)
        assertAdd(RealBigDec("2.2469135781975308642"), RealBigDec, RealBigDec)
    }

    @Test
    fun precision() {
        // N[Double, x] = RealDouble (no change to value)
        assertEquals(RealDouble(2.12345), N(RealDouble(2.12345), Integer(100)).eval())

        val a = RealBigDec("1.22222222223333333333")
        val b = RealBigDec("1.22222222223333333333111111111")

        // N[x_Precision] = Double
        assertEquals(RealDouble(1.2222222222333334), N(a).eval())
        assertEquals(RealDouble(1.2222222222333334), N(a).eval())

        // N[x_Precision, y] = Same if Y is larger than X
        assertEquals(30, N(b, Integer(40)).eval().precision)

        // N[x_Precision, y] = Y Precision if Y is smaller than X
        assertEquals(20, N(b, Integer(20)).eval().precision)
    }

    @Test
    fun precision_double_bigDec() {
        //PrecisionA + MachinePrecision = MachinePrecision
        val a = RealBigDec("0.33")
        val b = RealBigDec("0.3333333333")
        val c = RealDouble(1/3.0)

        assertEquals(RealDouble(0.6633333333333333), a + c)
        assertEquals(RealDouble(-0.0033333333333332993), a - c)
        assertEquals(RealBigDec("0.11"), a * b)
        assertEquals(RealDouble(0.9900000000000001), a / c)
    }

    @Test
    fun comparePrecision() {
        val a = RealBigDec("1.000000000000000000000000000000000001")
        val b = RealBigDec("1.000000000000000000000000000000000002")
        val c = RealDouble(1.0)

        assertEquals(0, a.compareTo(a))
        assertEquals(-1, a.compareTo(b))
        assertEquals(1, b.compareTo(a))

        // Double check equals is not getting truncated to double
        assertNotEquals(RealBigDec("2.000000000000000000000000000000000002"), a + b)
        assertEquals(RealBigDec("2.000000000000000000000000000000000003"), a + b)

        // When a double is added then we can truncate
        assertEquals(RealDouble(2.0), a + c)
    }

    @Test
    fun equals() {
        val n1 = RealDouble(5.0)
        val n2 = RealDouble(6.0)
        val n3 = RealDouble(5.0)
        val n4 = Integer(5)

        assertNotEquals(n1, n2)
        assertNotEquals(n1, n4)
        assertEquals(n1, n3)

        //Add BigDecimal cases or switch class to only use that type
    }

    @Test
    fun negate() {
        assertEquals(RealDouble(0.0), RealDouble(0.0).unaryMinus())
        assertEquals(RealDouble(-1.0), RealDouble(1.0).unaryMinus())
        assertEquals(RealDouble(1.0), RealDouble(-1.0).unaryMinus())
    }

    @Test
    fun typesNotEqual() {
        assertNotEquals(RealDouble(0.11662912394210093), RealBigDec("0.11662912394210093"))
    }
}