package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr
import org.junit.Assert.*


fun assertEqual(expected: Double, actual: Expr) {
    assertEquals(expected, actual.asReal().toDouble(), 0.001111) // TODO delta should be the largest possible value
}

fun assertEqual(expected: Int, actual: Expr) {
    assertEquals(expected, actual.asInteger().intValue())
}