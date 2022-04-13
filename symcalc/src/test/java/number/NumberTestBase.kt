package org.cerion.symcalc.number

import kotlin.test.assertEquals

abstract class NumberTestBase {

    fun assertAdd(expected: NumberExpr, a: NumberExpr, b: NumberExpr) {
        assertEquals(expected, a + b, "$a + $b")
        assertEquals(expected, b + a)
    }
}