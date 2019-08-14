package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.core.N
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class RealNumTest : NumberTestBase() {

    private val RealDouble = RealNum_Double(1.12345)
    private val RealBigDec = RealNum_BigDecimal("1.1234567890987654321")

    @Test
    fun addition() {
        assertAdd(RealNum_Double(2.2469067890987655), RealDouble, RealBigDec)
        assertAdd(RealNum_BigDecimal("2.2469135781975308642"), RealBigDec, RealBigDec)
    }

    @Test
    fun precision() {
        // N[Double, x] = RealDouble (no change to value)
        assertEquals(RealNum_Double(2.12345), N(RealNum_Double(2.12345), IntegerNum(100)).eval())

        val a = RealNum_BigDecimal("1.22222222223333333333")
        val b = RealNum_BigDecimal("1.22222222223333333333111111111")

        // N[x_Precision] = Double
        assertEquals(RealNum_Double(1.2222222222333334), N(a).eval())
        assertEquals(RealNum_Double(1.2222222222333334), N(a).eval())

        // N[x_Precision, y] = Same if Y is larger than X
        assertEquals(30, N(b, IntegerNum(40)).eval().precision)

        // N[x_Precision, y] = Y Precision if Y is smaller than X
        assertEquals(20, N(b, IntegerNum(20)).eval().precision)
    }

    @Test
    fun precision_double_bigDec() {
        //PrecisionA + MachinePrecision = MachinePrecision
        val a = RealNum_BigDecimal("0.33")
        val b = RealNum_BigDecimal("0.3333333333")
        val c = RealNum_Double(1/3.0)

        assertEquals(RealNum_Double(0.6633333333333333), a + c)
        assertEquals(RealNum_Double(-0.0033333333333332993), a - c)
        assertEquals(RealNum_BigDecimal("0.11"), a * b)
        assertEquals(RealNum_Double(0.9900000000000001), a / c)
    }

    @Test
    fun comparePrecision() {
        val a = RealNum_BigDecimal("1.000000000000000000000000000000000001")
        val b = RealNum_BigDecimal("1.000000000000000000000000000000000002")
        val c = RealNum_Double(1.0)

        assertEquals(0, a.compareTo(a))
        assertEquals(-1, a.compareTo(b))
        assertEquals(1, b.compareTo(a))

        // Double check equals is not getting truncated to double
        assertNotEquals(RealNum_BigDecimal("2.000000000000000000000000000000000002"), a + b)
        assertEquals(RealNum_BigDecimal("2.000000000000000000000000000000000003"), a + b)

        // When a double is added then we can truncate
        assertEquals(RealNum_Double(2.0), a + c)
    }

    @Test
    fun equals() {
        val n1 = RealNum_Double(5.0)
        val n2 = RealNum_Double(6.0)
        val n3 = RealNum_Double(5.0)
        val n4 = IntegerNum(5)

        assertNotEquals(n1, n2)
        assertNotEquals(n1, n4)
        assertEquals(n1, n3)

        //Add BigDecimal cases or switch class to only use that type
    }

    @Test
    fun negate() {
        assertEquals(RealNum_Double(0.0), RealNum_Double(0.0).unaryMinus())
        assertEquals(RealNum_Double(-1.0), RealNum_Double(1.0).unaryMinus())
        assertEquals(RealNum_Double(1.0), RealNum_Double(-1.0).unaryMinus())
    }

    @Test
    fun typesNotEqual() {
        assertNotEquals(RealNum_Double(0.11662912394210093), RealNum_BigDecimal("0.11662912394210093"))
    }
}