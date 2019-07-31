package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class CompoundExpression(vararg e: Expr) : FunctionExpr(Function.COMPOUND_EXPRESSION, *e) {

    override fun evaluate(): Expr {
        return get(size - 1)
    }

    override fun validate() {
    }
}
