package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.junit.Assert.*


fun assertEqual(expected: Double, actual: Expr) {
    assertEquals(expected, actual.asReal().toDouble(), 0.001111) // TODO delta should be the largest possible value
}

fun assertEqual(expected: Int, actual: Expr) {
    assertEquals(expected, actual.asInteger().intValue())
}

fun assertTrue(expected: Expr) {
    assertTrue(expected.asBool().value())
}

fun assertFalse(expected: Expr) {
    assertFalse(expected.asBool().value())
}

fun listOfNumbers(vararg numbers: Number) : ListExpr {
    val list = ListExpr()
    for (number in numbers)
        list.add(NumberExpr.create(number))

    return list
}

