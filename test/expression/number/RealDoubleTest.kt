package expression.number

import org.cerion.symcalc.expression.number.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RealDoubleTest : NumberTestBase() {

    @Test
    fun identity() {
        val n = RealDouble(3.14)

        assertAdd(n, n, Integer.ZERO)
        assertAdd(n, n, Rational.ZERO)
        assertAdd(n, n, RealDouble(0.0))
        assertAdd(n, n, RealBigDec(BigDecimal(0.0)))
        assertAdd(n, n, Complex.ZERO)
    }

    @Test
    fun compare() {
        assertEquals(0, RealDouble(5.000000).compareTo(Integer(5)))
        assertEquals(-1, RealDouble(4.999999999).compareTo(Integer(5)))
        assertEquals(1, RealDouble(5.000000000001).compareTo(Integer(5)))

        assertEquals(0, RealDouble(0.3333333333333333).compareTo(Rational(1,3)))
        assertEquals(-1, RealDouble(0.3333333333333332).compareTo(Rational(1,3)))
        assertEquals(1, RealDouble(0.3333333333333334).compareTo(Rational(1,3)))

        assertEquals(0, RealDouble(5.12345).compareTo(RealDouble(5.12345)))
        assertEquals(-1, RealDouble(5.12344).compareTo(RealDouble(5.12345)))
        assertEquals(1, RealDouble(5.12346).compareTo(RealDouble(5.12345)))

        assertEquals(0, RealDouble(0.0001).compareTo(Complex(RealDouble((0.0001)), IntegerTest.zero)))
        assertFailsWith<UnsupportedOperationException> { RealDouble(5.0).compareTo(Complex(IntegerTest.zero, IntegerTest.one)) }
    }

    @Test
    fun addition() {
        assertEquals(RealDouble(10.0), RealDouble(5.0) + Integer(5))
        assertEquals(RealDouble(5.5), RealDouble(5.0) + Rational(1,2))
        assertEquals(RealDouble(9.9), RealDouble(5.0) + RealDouble(4.9))
        assertEquals(RealDouble(15.0), RealDouble(5.0) + RealBigDec(BigDecimal.TEN))
        assertEquals(Complex(RealDouble(10.0), Integer(4)), RealDouble(5.0) + Complex(5,4))
    }

    @Test
    fun subtract() {
        assertEquals(RealDouble(1.0), RealDouble(5.0) - Integer(4))
        assertEquals(RealDouble(4.5), RealDouble(5.0) - Rational(1,2))
        assertEquals(RealDouble(0.5), RealDouble(5.0) - RealDouble(4.5))
        assertEquals(RealDouble(-5.0), RealDouble(5.0) - RealBigDec(BigDecimal.TEN))
        assertEquals(Complex(RealDouble(-5.0), Integer(4)), RealDouble(5.0) - Complex(10,-4))
    }

    @Test
    fun times() {
        assertEquals(RealDouble(20.0), RealDouble(5.0) * Integer(4))
        assertEquals(RealDouble(2.5), RealDouble(5.0) * Rational(1,2))
        assertEquals(RealDouble(22.5), RealDouble(5.0) * RealDouble(4.5))
        assertEquals(RealDouble(50.0), RealDouble(5.0) * RealBigDec(BigDecimal.TEN))
        assertEquals(Complex(RealDouble(50.0), RealDouble(-20.0)), RealDouble(5.0) * Complex(10,-4))
    }

    @Test
    fun divide() {
        assertEquals(RealDouble(1.25), RealDouble(5.0) / Integer(4))
        assertEquals(RealDouble(10.0), RealDouble(5.0) / Rational(1,2))
        assertEquals(RealDouble(2.0), RealDouble(5.0) / RealDouble(2.5))
        assertEquals(RealDouble(0.5), RealDouble(5.0) / RealBigDec(BigDecimal.TEN))
        assertEquals(Complex(RealDouble(5.0), RealDouble(-10.0)), RealDouble(50.0) / Complex(2,4))
    }
}