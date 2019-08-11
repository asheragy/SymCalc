package org.cerion.symcalc.expression.number

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ComplexNumTest {

    @Test
    fun equals() {
        assertEquals(ComplexNum(0, 0), ComplexNum.ZERO)
        assertEquals(ComplexNum(1, 0).eval(), IntegerNum.ONE)
        assertNotEquals(ComplexNum(1, 1), IntegerNum.ONE)
        assertEquals(ComplexNum(5, 6), ComplexNum(5, 6))
        assertNotEquals(ComplexNum(5, 6), ComplexNum(5, 7))
        assertNotEquals(ComplexNum(5, 6), ComplexNum(6, 6))
    }

    @Test
    fun toString_test() {
        assertEquals("1+5i", ComplexNum(1,5).toString())
        assertEquals("1-5i", ComplexNum(1,-5).toString())
    }

    @Test
    fun negate() {
        assertEquals(ComplexNum(0, 0), ComplexNum(0, 0).unaryMinus())
        assertEquals(ComplexNum(-1, 0), ComplexNum(1, 0).unaryMinus())
        assertEquals(ComplexNum(0, -1), ComplexNum(0, 1).unaryMinus())
        assertEquals(ComplexNum(-1, -1), ComplexNum(1, 1).unaryMinus())
        assertEquals(ComplexNum(1, 0), ComplexNum(-1, 0).unaryMinus())
        assertEquals(ComplexNum(0, 1), ComplexNum(0, -1).unaryMinus())
        assertEquals(ComplexNum(1, 1), ComplexNum(-1, -1).unaryMinus())
        assertEquals(ComplexNum(-1, 1), ComplexNum(1, -1).unaryMinus())
        assertEquals(ComplexNum(1, -1), ComplexNum(-1, 1).unaryMinus())
    }

    @Test
    fun conjugate() {
        assertEquals(ComplexNum(5,-5), ComplexNum(5,5).conjugate())
    }

    @Test
    fun addition() {
        assertEquals(ComplexNum(6,1), ComplexNum(1,1) + IntegerNum(5))
        assertEquals(ComplexNum(Rational(3,2),IntegerNum.ONE), ComplexNum(1,1) + Rational.HALF)
        assertEquals(ComplexNum(RealNum.create(6.0),IntegerNum.ONE), ComplexNum(1,1) + RealNum.create(5.0))
        assertEquals(ComplexNum(2,2), ComplexNum(1,1) + ComplexNum(1,1))
    }

    @Test
    fun subtraction() {
        assertEquals(ComplexNum(-4,1), ComplexNum(1,1) - IntegerNum(5))
        assertEquals(ComplexNum(Rational(1,2),IntegerNum.ONE), ComplexNum(1,1) - Rational.HALF)
        assertEquals(ComplexNum(RealNum.create(-4.0),IntegerNum.ONE), ComplexNum(1,1) - RealNum.create(5.0))
        assertEquals(IntegerNum.ZERO, ComplexNum(1,1) - ComplexNum(1,1))
    }

    @Test
    fun divide_nonComplex() {
        assertEquals(ComplexNum(2,4), ComplexNum(4,8) / IntegerNum.TWO)
        assertEquals(ComplexNum(Rational(3,2), Rational(7, 2)), ComplexNum(3,7) / IntegerNum.TWO)

        assertEquals(ComplexNum(9, 21), ComplexNum(3,7) / Rational(1,3))

        assertEquals(ComplexNum(RealNum.create(1.3636363636363635), RealNum.create(3.1818181818181817)), ComplexNum(3,7) / RealNum.create(2.2))
    }

    @Test
    fun divide_Complex() {
        // Divide: all integers
        assertEquals(ComplexNum(Rational(29,65), Rational(-2, 65)), ComplexNum(2,3) / ComplexNum(4,7))
        assertEquals(ComplexNum(Rational(6,25), Rational(17, 25)), ComplexNum(3,2) / ComplexNum(4,-3))

        // Divide: real
        assertEquals(ComplexNum(RealNum.create(0.4461538461538462), RealNum.create(-0.03076923076923077)), ComplexNum(RealNum.create(2.0),IntegerNum(3)) / ComplexNum(4,7))
    }

    @Test
    fun reducesToNonComplex() {
        assertEquals(NumberType.INTEGER, (ComplexNum(2,0) + IntegerNum(3)).numType)
        assertEquals(NumberType.INTEGER , (ComplexNum(2,10) + ComplexNum(3, -10)).numType)
        assertEquals(NumberType.REAL , (ComplexNum(2,10) - ComplexNum(RealNum.create(3.0), RealNum.create(10.0))).numType)
        assertEquals(NumberType.INTEGER, (ComplexNum(1,1) * ComplexNum(1, -1)).numType)
        assertEquals(NumberType.RATIONAL, (ComplexNum(1,0) / ComplexNum(2, 0)).numType)
    }

    @Test
    fun pow_realOnly() {
        assertEquals(IntegerNum(1), ComplexNum(2,0).pow(0))
        assertEquals(IntegerNum(2), ComplexNum(2,0).pow(1))
        assertEquals(IntegerNum(4), ComplexNum(2,0).pow(2))
        assertEquals(IntegerNum(8), ComplexNum(2,0).pow(3))
        assertEquals(IntegerNum("4294967296"), ComplexNum(2,0).pow(32))
        assertEquals(Rational(1,2), ComplexNum(2,0).pow(-1))
        assertEquals(Rational(1,4), ComplexNum(2,0).pow(-2))
        assertEquals(Rational(1,32), ComplexNum(2,0).pow(-5))
    }

    @Test
    fun pow() {
        assertEquals(ComplexNum(0, -177147), ComplexNum(0,3).pow(11))
        assertEquals(IntegerNum(4096), ComplexNum(1,1).pow(24))
        assertEquals(Rational(1, 4096), ComplexNum(1,1).pow(-24))
        assertEquals(ComplexNum(4096, 4096), ComplexNum(1,1).pow(25))
        assertEquals(ComplexNum(103595049,-51872200), ComplexNum(5,-4).pow(10))

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