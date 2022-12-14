package org.cerion.symcalc.expression

import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.integer.Fibonacci
import org.cerion.symcalc.function.trig.Cos
import org.cerion.symcalc.function.trig.Sin
import org.cerion.symcalc.number.*
import kotlin.test.*

class ExprTest {

    @Test
    fun isInteger() {
        assertTrue(Integer(5).isInteger)
        assertFalse(RealDouble(5.0).isInteger)
        assertFalse(Rational(1,2).isInteger)
        assertFalse(Complex(1,1).isInteger)

        assertFalse(ListExpr().isInteger)
    }

    @Test
    fun evalutationExceptionGenerates_errorExpr() {
        val bigInt = Integer("10000000000") // .intValue() should throw exception
        assertEquals(Expr.ExprType.ERROR, Fibonacci(bigInt).eval().type)
        assertEquals(Expr.ExprType.ERROR, Power(RealBigDec("3.0"), bigInt).eval().type)
    }

    @Test
    fun hashCodeTest() {
        assertEquals(Cos(VarExpr("x")).hashCode(), Cos(VarExpr("x")).hashCode())
        assertEquals(ListExpr(1,VarExpr("x")).hashCode(), ListExpr(1, VarExpr("x")).hashCode())
        assertEquals(Complex(1,2).hashCode(), Complex(1,2).hashCode())

        assertNotEquals(Sin(VarExpr("x")).hashCode(), Cos(VarExpr("x")).hashCode())
        assertNotEquals(Sin(VarExpr("x")).hashCode(), ListExpr(VarExpr("x")).hashCode())
    }
}