package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble
import kotlin.test.assertEquals


fun assertAll(vararg exprs: Unit) {
    org.junit.jupiter.api.assertAll(exprs.map {
        { it }
    })
}


infix fun Expr.`==`(expected: String) = evalEquals(RealBigDec(expected), this)
infix fun Expr.`==`(expected: Double) = evalEquals(RealDouble(expected), this)
infix fun Expr.`==`(expected: Int) = evalEquals(Integer(expected), this)
infix fun Expr.`==`(expected: Expr) = evalEquals(expected, this)
infix fun Expr.`==`(expected: List<Any>) = evalEquals(ListExpr(*expected.toTypedArray()), this)

private fun evalEquals(expected: Expr, actual: Expr) {
    assertEquals(expected, actual.eval(), "$actual")
}

@Deprecated("use ==")
infix fun Expr.`should equal`(expected: Expr) {
    assertEquals(expected, this)
}

@Deprecated("use ==")
infix fun Expr.`should equal`(expected: Double) {
    assertEquals(RealDouble(expected), this)
}

@Deprecated("use ==")
infix fun Expr.`should equal`(expected: Int) {
    assertEquals(Integer(expected), this)
}

@Deprecated("use ==")
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