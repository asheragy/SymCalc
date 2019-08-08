package org.cerion.symcalc.expression.number

import kotlin.test.assertEquals

abstract class NumberTestBase {

    fun assertIdentity(n: NumberExpr) {
        assertAdd(n, n, IntegerNum.ZERO)
        assertAdd(n, n, Rational.ZERO)
        assertAdd(n, n, RealNum.create(0.0))
        // This makes the number lose precision so not sure if its valid or not
        //assertAdd(n, n, RealNum.create(BigDecimal(0.0)))
        assertAdd(n, n, ComplexNum.ZERO)
    }

    fun assertAdd(expected: NumberExpr, a: NumberExpr, b: NumberExpr) {
        assertEquals(expected, a + b)
        assertEquals(expected, b + a)
    }
}