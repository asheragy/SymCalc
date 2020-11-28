package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr

class Hold(vararg e: Expr) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.HOLD.value

    override fun evaluate(): Expr {
        return this
    }

    override fun validate() {
    }
}
