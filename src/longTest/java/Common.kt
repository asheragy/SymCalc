package org.cerion.symcalc

import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr
import org.junit.Assert


fun run(times: Int, expr: Expr) {
    repeat(times) {
        run(expr)
    }
}

fun run(expr: Expr) {
    val res = expr.eval()
    if (res is ErrorExpr)
        Assert.fail(res.toString())
}