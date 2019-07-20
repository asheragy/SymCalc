package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class CompoundExpression(vararg e: Expr) : FunctionExpr(Function.COMPOUND_EXPRESSION, *e) {

    override fun evaluate(): Expr {
        all // eval all
        return get(size - 1)
    }
}
