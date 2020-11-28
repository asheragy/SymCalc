package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.integer.Fibonacci
import org.cerion.symcalc.expression.number.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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
}