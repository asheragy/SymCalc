package org.cerion.symcalc.expression.number

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ComplexTest {

    @Test
    fun equals() {
        assertEquals(Complex(0, 0), Complex.ZERO)
        assertEquals(Complex(1, 0).eval(), IntegerNum.ONE)
        assertNotEquals(Complex(1, 1), IntegerNum.ONE)
        assertEquals(Complex(5, 6), Complex(5, 6))
        assertNotEquals(Complex(5, 6), Complex(5, 7))
        assertNotEquals(Complex(5, 6), Complex(6, 6))
    }

    @Test
    fun toString_test() {
        assertEquals("1+5i", Complex(1,5).toString())
        assertEquals("1-5i", Complex(1,-5).toString())
    }

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
        assertEquals(Complex(6,1), Complex(1,1) + IntegerNum(5))
        assertEquals(Complex(Rational(3,2),IntegerNum.ONE), Complex(1,1) + Rational.HALF)
        assertEquals(Complex(RealNum_Double(6.0),IntegerNum.ONE), Complex(1,1) + RealNum_Double(5.0))
        assertEquals(Complex(2,2), Complex(1,1) + Complex(1,1))
    }

    @Test
    fun subtraction() {
        assertEquals(Complex(-4,1), Complex(1,1) - IntegerNum(5))
        assertEquals(Complex(Rational(1,2),IntegerNum.ONE), Complex(1,1) - Rational.HALF)
        assertEquals(Complex(RealNum_Double(-4.0),IntegerNum.ONE), Complex(1,1) - RealNum_Double(5.0))
        assertEquals(IntegerNum.ZERO, Complex(1,1) - Complex(1,1))
    }

    @Test
    fun divide_nonComplex() {
        assertEquals(Complex(2,4), Complex(4,8) / IntegerNum.TWO)
        assertEquals(Complex(Rational(3,2), Rational(7, 2)), Complex(3,7) / IntegerNum.TWO)

        assertEquals(Complex(9, 21), Complex(3,7) / Rational(1,3))

        assertEquals(Complex(RealNum_Double(1.3636363636363635), RealNum_Double(3.1818181818181817)), Complex(3,7) / RealNum_Double(2.2))
    }

    @Test
    fun divide_Complex() {
        // Divide: all integers
        assertEquals(Complex(Rational(29,65), Rational(-2, 65)), Complex(2,3) / Complex(4,7))
        assertEquals(Complex(Rational(6,25), Rational(17, 25)), Complex(3,2) / Complex(4,-3))

        // Divide: real
        assertEquals(Complex(RealNum_Double(0.4461538461538462), RealNum_Double(-0.03076923076923077)), Complex(RealNum_Double(2.0),IntegerNum(3)) / Complex(4,7))
    }

    @Test
    fun reducesToNonComplex() {
        assertEquals(NumberType.INTEGER, (Complex(2,0) + IntegerNum(3)).numType)
        assertEquals(NumberType.INTEGER , (Complex(2,10) + Complex(3, -10)).numType)
        assertEquals(NumberType.REAL_DOUBLE , (Complex(2,10) - Complex(RealNum_Double(3.0), RealNum_Double(10.0))).numType)
        assertEquals(NumberType.INTEGER, (Complex(1,1) * Complex(1, -1)).numType)
        assertEquals(NumberType.RATIONAL, (Complex(1,0) / Complex(2, 0)).numType)
    }

    @Test
    fun pow_realOnly() {
        assertEquals(IntegerNum(1), Complex(2,0).pow(0))
        assertEquals(IntegerNum(2), Complex(2,0).pow(1))
        assertEquals(IntegerNum(4), Complex(2,0).pow(2))
        assertEquals(IntegerNum(8), Complex(2,0).pow(3))
        assertEquals(IntegerNum("4294967296"), Complex(2,0).pow(32))
        assertEquals(Rational(1,2), Complex(2,0).pow(-1))
        assertEquals(Rational(1,4), Complex(2,0).pow(-2))
        assertEquals(Rational(1,32), Complex(2,0).pow(-5))
    }

    @Test
    fun pow() {
        assertEquals(Complex(0, -177147), Complex(0,3).pow(11))
        assertEquals(IntegerNum(4096), Complex(1,1).pow(24))
        assertEquals(Rational(1, 4096), Complex(1,1).pow(-24))
        assertEquals(Complex(4096, 4096), Complex(1,1).pow(25))
        assertEquals(Complex(103595049,-51872200), Complex(5,-4).pow(10))

        /* From before refactoring to iterative, probably unnecessary to have too many tests since number types are unimportant now
        assertEquals(IntegerNum(10000), Power(ComplexNum(0,10), IntegerNum(4)).eval())
        assertEquals(ComplexNum(0, 100000), Power(ComplexNum(0,10), IntegerNum(5)).eval())
        assertEquals(IntegerNum(-1000000), Power(ComplexNum(0,10), IntegerNum(6)).eval())
        assertEquals(ComplexNum(0, -10000000), Power(ComplexNum(0,10), IntegerNum(7)).eval())

        assertEquals(Rational(1,10000), Power(ComplexNum(0,10), IntegerNum(-4)).eval())
        assertEquals(ComplexNum(IntegerNum.ZERO, Rational(-1,100000)), Power(ComplexNum(0,10), IntegerNum(-5)).eval())
        assertEquals(Rational(-1,1000000), Power(ComplexNum(0,10), IntegerNum(-6)).eval())
        assertEquals(ComplexNum(IntegerNum.ZERO, Rational(1,10000000)), Power(ComplexNum(0,10), IntegerNum(-7)).eval())

        // Non integer imaginary part
        assertEquals(ComplexNum(IntegerNum.ZERO, RealNum.create(3125.0)), Power(ComplexNum(0.0,5.0), IntegerNum(5)).eval())
        assertEquals(ComplexNum(IntegerNum.ZERO, RealNum.create(-0.00032)), Power(ComplexNum(0.0,5.0), IntegerNum(-5)).eval())

        assertEquals(ComplexNum(IntegerNum.ZERO, Rational(243,32)), Power(ComplexNum(Rational.ZERO,Rational(3,2)), IntegerNum(5)).eval())
        assertEquals(ComplexNum(IntegerNum.ZERO, Rational(-32,243)), Power(ComplexNum(Rational.ZERO,Rational(3,2)), IntegerNum(-5)).eval())
        assertEquals(ComplexNum(IntegerNum.ZERO, Rational(1,10000000)), Power(ComplexNum(0,10), IntegerNum(-7)).eval())
         */
    }
}