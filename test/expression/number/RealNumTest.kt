package org.cerion.symcalc.expression.number

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

        // TODO add these tests and probably same for other operators
        // Wrap all operators in a function that converts both BigDec to the lowest precision of the 2
        /*
PrecisionA + PrecisionB = Precision of lowest
PrecisionA + MachinePrecision = MachinePrecision
 */
    }

    @Test
    fun comparePrecision() {
        val a = RealNum.create("1.000000000000000000000000000000000001")
        val b = RealNum.create("1.000000000000000000000000000000000002")
        val c = RealNum.create(1.0)

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