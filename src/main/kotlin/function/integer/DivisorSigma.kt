package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr

class DivisorSigma (vararg e: Any) : FunctionExpr(*e) {
    override fun evaluate(): Expr {
        return this
    }
}