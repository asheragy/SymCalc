package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.NumberExpr
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun assertEqual(expected: Int, actual: Expr) {
    assertEquals(expected, actual.asInteger().intValue())
}

fun assertTrue(expected: Expr) {
    assertTrue(expected.asBool().value)
}

fun listOfNumbers(vararg numbers: Number) : ListExpr {
    val list = ListExpr()
    for (number in numbers)
        list.add(NumberExpr.create(number))

    return list
}

