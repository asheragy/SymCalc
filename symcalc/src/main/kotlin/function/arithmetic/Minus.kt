package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer

class Minus(vararg e: Expr) : FunctionExpr(*e) {
    override fun evaluate(): Expr {
        return Integer.NEGATIVE_ONE * get(0)
    }

    override fun validate() {
        validateParameterCount(1)
    }

    override val properties: Int
        get() = Properties.LISTABLE.value
}