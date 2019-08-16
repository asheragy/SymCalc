package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer

class Minus(vararg e: Expr) : FunctionExpr(Function.MINUS, *e) {
    override fun evaluate(): Expr {
        return Times(Integer.NEGATIVE_ONE, get(0)).eval()
    }

    override fun validate() {
        validateParameterCount(1)
    }

    override val properties: Int
        get() = Expr.Properties.LISTABLE.value
}