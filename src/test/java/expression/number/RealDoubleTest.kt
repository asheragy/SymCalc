package org.cerion.symcalc.expression.number

import org.cerion.symcalc.`should equal`
import org.junit.jupiter.api.Assertions
import java.math.BigDecimal
import kotlin.test.Test
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

        assertEquals(0, RealDouble(0.0001).compareTo(Complex(0.0001, 0)))
        assertFailsWith<UnsupportedOperationException> { RealDouble(5.0).compareTo(Complex(0, 1)) }
    }

    @Test
    fun addition() {
        assertEquals(RealDouble(10.0), RealDouble(5.0) + Integer(5))
        assertEquals(RealDouble(5.5), RealDouble(5.0) + Rational(1,2))
        assertEquals(RealDouble(9.9), RealDouble(5.0) + RealDouble(4.9))
        assertEquals(RealDouble(15.0), RealDouble(5.0) + RealBigDec(BigDecimal.TEN))
        assertEquals(Complex(10.0, 4), RealDouble(5.0) + Complex(5,4))
    }

    @Test
    fun subtract() {
        assertEquals(RealDouble(1.0), RealDouble(5.0) - Integer(4))
        assertEquals(RealDouble(4.5), RealDouble(5.0) - Rational(1,2))
        assertEquals(RealDouble(0.5), RealDouble(5.0) - RealDouble(4.5))
        assertEquals(RealDouble(-5.0), RealDouble(5.0) - RealBigDec(BigDecimal.TEN))
        assertEquals(Complex(-5.0, 4), RealDouble(5.0) - Complex(10,-4))
    }

    @Test
    fun times() {
        assertEquals(RealDouble(20.0), RealDouble(5.0) * Integer(4))
        assertEquals(RealDouble(2.5), RealDouble(5.0) * Rational(1,2))
        assertEquals(RealDouble(22.5), RealDouble(5.0) * RealDouble(4.5))
        assertEquals(RealDouble(50.0), RealDouble(5.0) * RealBigDec(BigDecimal.TEN))
        assertEquals(Complex(50.0, -20.0), RealDouble(5.0) * Complex(10,-4))
    }

    @Test
    fun divide() {
        assertEquals(RealDouble(1.25), RealDouble(5.0) / Integer(4))
        assertEquals(RealDouble(10.0), RealDouble(5.0) / Rational(1,2))
        assertEquals(RealDouble(2.0), RealDouble(5.0) / RealDouble(2.5))
        assertEquals(RealDouble(0.5), RealDouble(5.0) / RealBigDec(BigDecimal.TEN))
        assertEquals(Complex(5.0, -10.0), RealDouble(50.0) / Complex(2,4))
    }

    @Test
    fun pow() {
        // Integer
        RealDouble(5.0) pow Integer(4) `should equal` 625.0

        // Rational
        RealDouble(5.0) pow Rational(1,2) `should equal` 2.23606797749979
        RealDouble(1.2345) pow Rational(12345,179) `should equal` 2040886.0816112224
        RealDouble(1.2345) pow Rational(-12345,179) `should equal` 4.89983252377579E-7
        RealDouble(1.2345) pow Rational(3,2) `should equal` 1.3716289453146575

        // Double
        RealDouble(5.0) pow RealDouble(2.5) `should equal` 55.90169943749474

        // BigDec
        RealDouble(3.14) pow RealBigDec("3.14") `should equal` 36.33783888017471
        RealDouble(5.0) pow RealBigDec("3.0") `should equal` 125.0
    }

    @Test
    fun pow_negativeRoot() {
        assertEquals(Complex(1.224646799147353E-16,1.9999999999999998), RealDouble(-4.0) pow RealDouble(0.5))
        assertEquals(Complex(1.0, 1.7320508075688767), RealDouble(-8.0) pow RealDouble(1/3.0))
        assertEquals(Complex(-2.5196414962461827E-16, -1.3716289453146575), RealDouble(-1.2345) pow RealDouble(1.5))

    }

    @Test
    fun floor() {
        Assertions.assertEquals(Integer(5), RealDouble(5.00000000001).floor())
        Assertions.assertEquals(Integer(-4), RealDouble(-3.4).floor())
    }

    @Test
    fun mod() {
        RealDouble(5.0) % Integer(3) `should equal` 2.0
        RealDouble(1.5) % Rational(10,3) `should equal` 1.5
        RealDouble(2.6) % RealDouble(2.5) `should equal` 0.10000000000000009
        RealDouble(-2.6) % RealDouble(2.5) `should equal` 2.4
        RealDouble(-2.6) % RealBigDec("2.5") `should equal` 2.4
    }

    @Test
    fun modComplex() {
        RealDouble(3000.0) % Complex(37, 226) `should equal` Complex(-12.0, 29)
        RealDouble(-3000.0) % Complex(37, -226) `should equal` Complex(12.0, 29)
    }

    @Test
    fun quotient() {
        RealDouble(5.2).quotient(Integer(2)) `should equal` 2
        RealDouble(-5.2).quotient(Integer(2)) `should equal` -3

        RealDouble(5.2).quotient(Rational(1, 2)) `should equal` 10
        RealDouble(-5.2).quotient(Rational(1, 2)) `should equal` -11

        RealDouble(5.2).quotient(RealDouble(0.5)) `should equal` 10
        RealDouble(-5.2).quotient(RealDouble(0.5)) `should equal` -11

        RealDouble(5.2).quotient(RealBigDec("0.5")) `should equal` 10
        RealDouble(-5.2).quotient(RealBigDec("0.5")) `should equal` -11

        RealDouble(5.2).quotient(Complex(2,4)) `should equal` Complex(1, -1)
        RealDouble(-50.2).quotient(Complex(5,4)) `should equal` Complex(-6, 5)
    }
}