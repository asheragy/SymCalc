package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Hold(vararg e: Expr) : FunctionExpr(Function.HOLD, *e) {

    override val properties: Int
        get() = Expr.Properties.HOLD.value

    override fun evaluate(): Expr {
        return this
    }

    override fun validate() {
        validateParameterCount(1)
    }
}
