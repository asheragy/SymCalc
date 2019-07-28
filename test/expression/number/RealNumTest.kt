package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.core.N
import org.junit.Assert.*
import org.junit.Test

import java.math.BigDecimal
import kotlin.test.assertFalse

class RealNumTest : NumberTestBase() {

    private val RealDouble = RealNum.create(1.12345)
    private val RealBigDec = RealNum.create(BigDecimal("1.1234567890987654321"))

    @Test
    fun identity() {
        assertIdentity(RealDouble)
        assertIdentity(RealBigDec)
    }

    @Test
    fun constructor_createsCorrectType() {
        assertEquals(true, RealNum.create(1.2345).isDouble)
        assertEquals(false, RealNum.create(BigDecimal("1.12345")).isDouble)

        assertTrue(RealNum.create("1.2345").isDouble)
        assertFalse(RealNum.create("1.1234567899999987654321").isDouble)
        assertEquals(22, RealNum.create("1.1234567899999987654321").precision)
    }

    @Test
    fun addition() {
        assertAdd(RealNum.create(2.2469067890987655), RealDouble, RealBigDec)
        assertAdd(RealNum.create("2.2469135781975308642"), RealBigDec, RealBigDec)
    }

    @Test
    fun precision() {
        // N[Double, x] = RealDouble (no change to value)
        assertEquals(RealNum.create(2.12345), N(RealNum.create(2.12345), IntegerNum(100)).eval())

        val a = RealNum.create("1.22222222223333333333")
        val b = RealNum.create("1.222222222233333333331111111111")

        // N[x_Precision] = Double
        assertTrue(N(a).eval().asReal().isDouble)
        assertEquals(RealNum.create(1.2222222222333334), N(a).eval())

        // N[x_Precision, y] = Same if Y is larger than X
        assertEquals(30, N(b, IntegerNum(40)).eval().asReal().precision)

        // N[x_Precision, y] = Y Precision if Y is smaller than X
        assertEquals(20, N(b, IntegerNum(20)).eval().asReal().precision)


    }

    @Test
    fun precision_Arithmetic() {
        val a = RealNum_BigDecimal("0.33")
        val b = RealNum_BigDecimal("0.3333333333")

        // PrecisionA + PrecisionB = Precision of lowest
        assertEquals(RealNum_BigDecimal("0.66"), a + b)
        assertEquals(RealNum_BigDecimal("0.00"), a - b)
        assertEquals(RealNum_BigDecimal("0.11"), a * b)
        assertEquals(RealNum_BigDecimal("0.99"), a / b)

        //PrecisionA + MachinePrecision = MachinePrecision
        val c = RealNum.create(1/3.0)
        assertEquals(RealNum.create(0.6633333333333333), a + c)
        assertEquals(RealNum.create(-0.0033333333333332993), a - c)
        assertEquals(RealNum.create(0.11), a * b)
        // TODO check this one its different in mathematica
        assertEquals(RealNum.create(1.01010101010101), a / c)

        assertEquals("5.85987", Plus(N(E(), IntegerNum(10)), N(Pi(), IntegerNum(5))).eval().toString())
    }

    @Test
    fun precision_Rounding() {
        // TODO N[1/3,2] + N[1/3,10] = 0.67
        // 0.33 + 0.3333333333 = 0.66
        // When evaluated 1/3 is stored at higher precision for calculations but ultimately displays as whatever was specified
    }

    @Test
    fun comparePrecision() {
        val a = RealNum.create("1.000000000000000000000000000000000001")
        val b = RealNum.create("1.000000000000000000000000000000000002")
        //val c = RealNum.create(1.0)

        assertEquals(0, a.compareTo(a))
        assertEquals(-1, a.compareTo(b))
        assertEquals(1, b.compareTo(a))

        // Double check equals is not getting truncated to double
        assertNotEquals(RealNum.create("2.000000000000000000000000000000000002"), a + b)
        assertEquals(RealNum.create("2.000000000000000000000000000000000003"), a + b)

        // When a double is added then we can truncate
        assertEquals(RealNum.create(2.0), a + b)
    }

    @Test
    fun equals() {

        val n1 = RealNum.create(5.0)
        val n2 = RealNum.create(6.0)
        val n3 = RealNum.create(5.0)
        val n4 = IntegerNum(5)

        assertNotEquals(n1, n2)
        assertEquals(n1, n4)
        assertEquals(n1, n3)

        //Add BigDecimal cases or switch class to only use that type
    }

    @Test
    fun negate() {
        assertEquals(0.0, RealNum.create(0.0).unaryMinus().toDouble(), 0.01)
        assertEquals(-1.0, RealNum.create(1.0).unaryMinus().toDouble(), 0.01)
        assertEquals(1.0, RealNum.create(-1.0).unaryMinus().toDouble(), 0.01)
    }
}