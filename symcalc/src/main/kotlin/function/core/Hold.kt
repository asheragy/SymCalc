package org.cerion.symcalc.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr

class Hold(vararg e: Expr) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.HoldAll.value

    override fun evaluate(): Expr {
        return this
    }

    override fun validate() {
    }
}
