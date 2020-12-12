package org.cerion.symcalc.function.trig

import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import kotlin.test.assertEquals

fun assertTrigExprRange(expected: ListExpr, f: SymbolExpr) {
    val size = expected.size
    val n = size / 2
    val start = -n * 5
    val end = n * 5

    // Test cycles with increments of Pi / N
    val step = Times(Pi(), Rational(1,n))
    for(i in start until end) {
        val pos = (((i % size) + size) % size) // mod but handles negative values
        val x = Times(Integer(i), step)
        assertEquals(expected[pos], f.eval(x))
    }
}