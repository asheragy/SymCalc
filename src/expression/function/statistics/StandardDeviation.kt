package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class StandardDeviation(vararg e: Expr) : FunctionExpr(Function.STANDARD_DEVIATION, *e) {

    override fun evaluate(): Expr {
        throw NotImplementedError()
    }
}
