package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr


abstract class HyperbolicBase protected constructor(e: Any) : FunctionExpr(e) {
    override val properties: Int
        get() = Properties.Listable.value

    abstract fun evaluate(e: Expr): Expr

    override fun evaluate(): Expr {
        return evaluate(get(0))
    }
}
