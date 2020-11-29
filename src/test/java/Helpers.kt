package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.UnsupportedOperationException
import kotlin.test.assertEquals


fun assertAll(vararg exprs: () -> Unit) {
    org.junit.jupiter.api.assertAll(exprs.map {
        it
    })
}

infix fun Expr.`==`(expected: Expr): () -> Unit = {
    val actual = this.eval()
    val msg = "<$this>\n${" ".repeat(20)}"
    assertEquals(expected, actual, msg)
}

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