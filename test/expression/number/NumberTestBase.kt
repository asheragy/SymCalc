package org.cerion.symcalc.expression.number

import kotlin.test.assertEquals

abstract class NumberTestBase {

    fun assertIdentity(n: NumberExpr) {
        assertAdd(n, n, IntegerNum.ZERO)
        assertAdd(n, n, RationalNum.ZERO)
        //assertAdd(n, n, RealNum.ZERO)
        assertAdd(n, n, ComplexNum.ZERO)
        // TODO fix this and add more operators
    }

    fun assertAdd(expected: NumberExpr, a: NumberExpr, b: NumberExpr) {
        assertEquals(expected, a + b)
        assertEquals(expected, b + a)
    }
}