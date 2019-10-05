package org.cerion.symcalc.expression.number

import org.cerion.symcalc.`should equal`
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class ComplexTest {

    @Test
    fun equals() {
        assertEquals(Complex(0, 0), Complex.ZERO)
        assertEquals(Complex(1, 0).eval(), Integer.ONE)
        assertNotEquals(Complex(1, 1).eval(), Integer.ONE)
        assertEquals(Complex(5, 6), Complex(5, 6))
        assertNotEquals(Complex(5, 6), Complex(5, 7))
        assertNotEquals(Complex(5, 6), Complex(6, 6))
    }

    /*
    @Test
    fun toString_test() {
        assertEquals("1+5i", Complex(1,5).toString())
        assertEquals("1-5i", Complex(1,-5).toString())
    }
     */

    @Test
    fun negate() {
        assertEquals(Complex(0, 0), Complex(0, 0).unaryMinus())
        assertEquals(Complex(-1, 0), Complex(1, 0).unaryMinus())
        assertEquals(Complex(0, -1), Complex(0, 1).unaryMinus())
        assertEquals(Complex(-1, -1), Complex(1, 1).unaryMinus())
        assertEquals(Complex(1, 0), Complex(-1, 0).unaryMinus())
        assertEquals(Complex(0, 1), Complex(0, -1).unaryMinus())
        assertEquals(Complex(1, 1), Complex(-1, -1).unaryMinus())
        assertEquals(Complex(-1, 1), Complex(1, -1).unaryMinus())
        assertEquals(Complex(1, -1), Complex(-1, 1).unaryMinus())
    }

    @Test
    fun conjugate() {
        assertEquals(Complex(5,-5), Complex(5,5).conjugate())
    }

    @Test
    fun addition() {
        assertEquals(Complex(6,1), Complex(1,1) + Integer(5))
        assertEquals(Complex(Rational(3,2), 1), Complex(1,1) + Rational.HALF)
        assertEquals(Complex(6.0, 1), Complex(1,1) + RealDouble(5.0))
        assertEquals(Complex(2,2), Complex(1,1) + Complex(1,1))
    }

    @Test
    fun subtraction() {
        assertEquals(Complex(-4,1), Complex(1,1) - Integer(5))
        assertEquals(Complex(Rational(1,2), 1), Complex(1,1) - Rational.HALF)
        assertEquals(Complex(-4.0, 1), Complex(1,1) - RealDouble(5.0))
        assertEquals(Integer.ZERO, Complex(1,1) - Complex(1,1))
    }

    @Test
    fun divide_nonComplex() {
        assertEquals(Complex(2,4), Complex(4,8) / Integer.TWO)
        assertEquals(Complex(Rational(3,2), Rational(7, 2)), Complex(3,7) / Integer.TWO)

        assertEquals(Complex(9, 21), Complex(3,7) / Rational(1,3))

        assertEquals(Complex(1.3636363636363635, 3.1818181818181817), Complex(3,7) / RealDouble(2.2))
    }

    @Test
    fun divide_Complex() {
        // Divide: all integers
        assertEquals(Complex(Rational(29,65), Rational(-2, 65)), Complex(2,3) / Complex(4,7))
        assertEquals(Complex(Rational(6,25), Rational(17, 25)), Complex(3,2) / Complex(4,-3))

        // Divide: real
        assertEquals(Complex(0.4461538461538462, -0.03076923076923077), Complex(2.0, 3) / Complex(4,7))
    }

    @Test
    fun reducesToNonComplex() {
        assertEquals(NumberType.INTEGER, (Complex(2,0) + Integer(3)).numType)
        assertEquals(NumberType.INTEGER , (Complex(2,10) + Complex(3, -10)).numType)
        assertEquals(NumberType.REAL_DOUBLE , (Complex(2,10) - Complex(3.0, 10.0)).numType)
        assertEquals(NumberType.INTEGER, (Complex(1,1) * Complex(1, -1)).numType)
        assertEquals(NumberType.RATIONAL, (Complex(1,0) / Complex(2, 0)).numType)
    }

    @Test
    fun pow_realOnly() {
        assertEquals(Integer(1), Complex(2,0).pow(0))
        assertEquals(Integer(2), Complex(2,0).pow(1))
        assertEquals(Integer(4), Complex(2,0).pow(2))
        assertEquals(Integer(8), Complex(2,0).pow(3))
        assertEquals(Integer("4294967296"), Complex(2,0).pow(32))
        assertEquals(Rational(1,2), Complex(2,0).pow(-1))
        assertEquals(Rational(1,4), Complex(2,0).pow(-2))
        assertEquals(Rational(1,32), Complex(2,0).pow(-5))
    }

    @Test
    fun pow_toInteger() {
        assertEquals(Complex(0, -177147), Complex(0,3).pow(11))
        assertEquals(Integer(4096), Complex(1,1).pow(24))
        assertEquals(Rational(1, 4096), Complex(1,1).pow(-24))
        assertEquals(Complex(4096, 4096), Complex(1,1).pow(25))
        assertEquals(Complex(103595049,-51872200), Complex(5,-4).pow(10))

        // Rational
        assertEquals(Complex(0, Rational(243,32)), Complex(Rational.ZERO, Rational(3,2)) pow Integer(5))
        assertEquals(Complex(0, Rational(-32,243)), Complex(Rational.ZERO, Rational(3,2)) pow Integer(-5))

        // Double
        assertEquals(Complex(0.0, 3125.0), Complex(0.0,5.0) pow Integer(5))
        assertEquals(Complex(0.0, -0.00032), Complex(0.0,5.0) pow Integer(-5))
    }

    @Test
    fun pow_toRational() {
        assertEquals(Complex(1.7989074399478673,1.1117859405028423), Complex(2.0, 4) pow Rational.HALF)
        assertEquals(Complex(0.07690324994251796,-0.18717392051825588), Complex(2.0, 4) pow Rational(-16,15))
        assertEquals(Complex("1.536621", "0.5943189"), Complex("2.000001", 4) pow Rational.THIRD)
    }

    @Test
    fun pow_toReal() {
        // Double
        assertEquals(Complex(1.309544770737814, 6.174162506105573), Complex(2.0,4.0) pow RealDouble(1.23))
        assertEquals(Complex(0.03287406851910734, -0.1549926705899962), Complex(2,4) pow RealDouble(-1.23))

        // BigDec
        assertEquals(Complex(1.309544770737814, 6.174162506105573), Complex(2.0, 4) pow RealBigDec("1.23"))
        assertEquals(Complex("1.31", "6.17"), Complex(2,4) pow RealBigDec("1.23"))
    }

    @Test
    fun floor() {
        Assertions.assertEquals(Complex(2, 2), Complex("2.34", Rational(5, 2)).floor())
        Assertions.assertEquals(Integer(2), Complex("2.34", Rational(1, 2)).floor())
    }

    @Test
    fun quotient() {
        Complex(8, 7).quotient(Integer(3)) `should equal` Complex(3, 2)
        Complex(8, 8).quotient(Integer(3)) `should equal` Complex(3, 3)
        Complex(7, 3).quotient(Integer(2)) `should equal` Complex(4, 2)
        //Complex(7, 5).quotient(Integer(2)) `should equal` Complex(4, 2) // TODO_LP some ambiguity here 0.5 could round up or down

        Complex(7, 5).quotient(Rational(1, 2)) `should equal` Complex(14, 10)
        Complex(7, 5).quotient(RealDouble(0.33)) `should equal` Complex(21, 15)
        Complex(7, 5).quotient(RealBigDec("0.33")) `should equal` Complex(21, 15)

        Complex(7, 5).quotient(Complex(1, 2)) `should equal` Complex(3, -2)
    }
}