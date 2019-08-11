package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.integer.Fibonacci
import org.cerion.symcalc.expression.number.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.test.assertEquals

class ExprTest {

    @Test
    fun isInteger() {
        assertTrue(IntegerNum(5).isInteger)
        assertFalse(RealNum_Double(5.0).isInteger)
        assertFalse(Rational(1,2).isInteger)
        assertFalse(Complex(1,1).isInteger)

        assertFalse(ListExpr().isInteger)
    }

    @Test
    fun evalutationExceptionGenerates_errorExpr() {
        val bigInt = IntegerNum("10000000000") // .intValue() should throw exception
        assertEquals(Expr.ExprType.ERROR, Fibonacci(bigInt).eval().type)
        assertEquals(Expr.ExprType.ERROR, Power(RealNum_BigDecimal("3.0"), bigInt).eval().type)
    }
}