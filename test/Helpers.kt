package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.test.assertEquals

infix fun Expr.`should equal`(expected: Expr) {
    assertEquals(expected, this)
}

infix fun Expr.`should equal`(expected: Double) {
    assertEquals(RealDouble(expected), this)
}

infix fun Expr.`should equal`(expected: Int) {
    assertEquals(Integer(expected), this)
}

infix fun Expr.`should equal`(expected: String) {
    assertEquals(RealBigDec(expected), this)
}