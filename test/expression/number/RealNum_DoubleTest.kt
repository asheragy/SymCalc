package expression.number

import org.cerion.symcalc.expression.number.*
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class RealNum_DoubleTest : NumberTestBase() {

    @Test
    fun identity() {
        val n = RealNum_Double(3.14)

        assertAdd(n, n, IntegerNum.ZERO)
        assertAdd(n, n, Rational.ZERO)
        assertAdd(n, n, RealNum_Double(0.0))
        assertAdd(n, n, RealNum_BigDecimal(BigDecimal(0.0)))
        assertAdd(n, n, Complex.ZERO)
    }

    @Test
    fun compare() {
        assertEquals(0, RealNum_Double(5.000000).compareTo(IntegerNum(5)))
        assertEquals(-1, RealNum_Double(4.999999999).compareTo(IntegerNum(5)))
        assertEquals(1, RealNum_Double(5.000000000001).compareTo(IntegerNum(5)))

        assertEquals(0, RealNum_Double(0.3333333333333333).compareTo(Rational(1,3)))
        assertEquals(-1, RealNum_Double(0.3333333333333332).compareTo(Rational(1,3)))
        assertEquals(1, RealNum_Double(0.3333333333333334).compareTo(Rational(1,3)))

        assertEquals(0, RealNum_Double(5.12345).compareTo(RealNum_Double(5.12345)))
        assertEquals(-1, RealNum_Double(5.12344).compareTo(RealNum_Double(5.12345)))
        assertEquals(1, RealNum_Double(5.12346).compareTo(RealNum_Double(5.12345)))

        assertEquals(0, RealNum_Double(0.0001).compareTo(Complex(RealNum_Double((0.0001)), IntegerNumTest.zero)))
        assertFailsWith<UnsupportedOperationException> { RealNum_Double(5.0).compareTo(Complex(IntegerNumTest.zero, IntegerNumTest.one)) }
    }

    @Test
    fun addition() {
        assertEquals(RealNum_Double(10.0), RealNum_Double(5.0) + IntegerNum(5))
        assertEquals(RealNum_Double(5.5), RealNum_Double(5.0) + Rational(1,2))
        assertEquals(RealNum_Double(9.9), RealNum_Double(5.0) + RealNum_Double(4.9))
        assertEquals(RealNum_Double(15.0), RealNum_Double(5.0) + RealNum_BigDecimal(BigDecimal.TEN))
        assertEquals(Complex(RealNum_Double(10.0), IntegerNum(4)), RealNum_Double(5.0) + Complex(5,4))
    }

    @Test
    fun subtract() {
        assertEquals(RealNum_Double(1.0), RealNum_Double(5.0) - IntegerNum(4))
        assertEquals(RealNum_Double(4.5), RealNum_Double(5.0) - Rational(1,2))
        assertEquals(RealNum_Double(0.5), RealNum_Double(5.0) - RealNum_Double(4.5))
        assertEquals(RealNum_Double(-5.0), RealNum_Double(5.0) - RealNum_BigDecimal(BigDecimal.TEN))
        assertEquals(Complex(RealNum_Double(-5.0), IntegerNum(4)), RealNum_Double(5.0) - Complex(10,-4))
    }

    @Test
    fun times() {
        assertEquals(RealNum_Double(20.0), RealNum_Double(5.0) * IntegerNum(4))
        assertEquals(RealNum_Double(2.5), RealNum_Double(5.0) * Rational(1,2))
        assertEquals(RealNum_Double(22.5), RealNum_Double(5.0) * RealNum_Double(4.5))
        assertEquals(RealNum_Double(50.0), RealNum_Double(5.0) * RealNum_BigDecimal(BigDecimal.TEN))
        assertEquals(Complex(RealNum_Double(50.0), RealNum_Double(-20.0)), RealNum_Double(5.0) * Complex(10,-4))
    }

    @Test
    fun divide() {
        assertEquals(RealNum_Double(1.25), RealNum_Double(5.0) / IntegerNum(4))
        assertEquals(RealNum_Double(10.0), RealNum_Double(5.0) / Rational(1,2))
        assertEquals(RealNum_Double(2.0), RealNum_Double(5.0) / RealNum_Double(2.5))
        assertEquals(RealNum_Double(0.5), RealNum_Double(5.0) / RealNum_BigDecimal(BigDecimal.TEN))
        assertEquals(Complex(RealNum_Double(5.0), RealNum_Double(-10.0)), RealNum_Double(50.0) / Complex(2,4))
    }
}