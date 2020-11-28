package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.UnsupportedOperationException
import kotlin.test.assertEquals

/*
infix fun Expr.`should equal`(expected: Expr) {
    val eval = this.eval()
    //assertEquals(expected, this)
    val msg = """$this
                    Expected: $expected
                      Actual: $eval""".trimMargin()
    assertTrue(msg) { eval == expected }
}
 */

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
    val e: Expr = when {
        expected[0].isLetter() -> VarExpr(expected)
        expected.contains('.') -> RealBigDec(expected)
        expected.any { a -> a.isDigit() } -> Integer(expected)
        else -> throw UnsupportedOperationException()
    }

    assertEquals(e, this)
}

infix fun String.`should equal`(expected: String) {
    assertEquals(expected, this)
}