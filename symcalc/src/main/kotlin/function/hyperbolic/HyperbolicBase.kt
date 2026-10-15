package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.Attribute
import org.cerion.symcalc.function.FunctionExpr


abstract class HyperbolicBase protected constructor(e: Any) : FunctionExpr(e) {
    override val attributes: Set<Attribute>
        get() = setOf(Attribute.Listable)

    abstract fun evaluate(e: Expr): Expr

    override fun evaluate(): Expr {
        return evaluate(get(0))
    }
}
