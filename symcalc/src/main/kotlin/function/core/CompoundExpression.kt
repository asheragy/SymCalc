package org.cerion.symcalc.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr

class CompoundExpression(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        return get(size - 1)
    }

    override fun validate() {
    }
}
