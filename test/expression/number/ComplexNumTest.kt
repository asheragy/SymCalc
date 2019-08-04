package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.core.N
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
        assertEquals(ComplexNum(RationalNum(3,2),IntegerNum.ONE), ComplexNum(1,1) + RationalNum.HALF)
        assertEquals(ComplexNum(RealNum.create(6.0),IntegerNum.ONE), ComplexNum(1,1) + RealNum.create(5.0))
        assertEquals(ComplexNum(2,2), ComplexNum(1,1) + ComplexNum(1,1))
    }

    @Test
    fun subtraction() {
        assertEquals(ComplexNum(-4,1), ComplexNum(1,1) - IntegerNum(5))
        assertEquals(ComplexNum(RationalNum(1,2),IntegerNum.ONE), ComplexNum(1,1) - RationalNum.HALF)
        assertEquals(ComplexNum(RealNum.create(-4.0),IntegerNum.ONE), ComplexNum(1,1) - RealNum.create(5.0))
        assertEquals(ComplexNum(0,0), ComplexNum(1,1) - ComplexNum(1,1))
    }

    @Test
    fun divide_nonComplex() {
        assertEquals(ComplexNum(2,4), ComplexNum(4,8) / IntegerNum.TWO)
        assertEquals(ComplexNum(RationalNum(3,2), RationalNum(7, 2)), ComplexNum(3,7) / IntegerNum.TWO)

        assertEquals(ComplexNum(9, 21), ComplexNum(3,7) / RationalNum(1,3))

        assertEquals(ComplexNum(RealNum.create(1.3636363636363635), RealNum.create(3.1818181818181817)), ComplexNum(3,7) / RealNum.create(2.2))
    }

    @Test
    fun divide_Complex() {
        // Divide: all integers
        assertEquals(ComplexNum(RationalNum(29,65), RationalNum(-2, 65)), ComplexNum(2,3) / ComplexNum(4,7))
        assertEquals(ComplexNum(RationalNum(6,25), RationalNum(17, 25)), ComplexNum(3,2) / ComplexNum(4,-3))

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

}