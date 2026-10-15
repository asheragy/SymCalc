package org.cerion.symcalc.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.Attribute
import org.cerion.symcalc.function.FunctionExpr
import kotlin.collections.Set

class Hold(vararg e: Expr) : FunctionExpr(*e) {
    override val attributes: Set<Attribute>
        get() = setOf(Attribute.HoldAll)

    override fun evaluate(): Expr {
        return this
    }

    override fun validate() {
    }
}
