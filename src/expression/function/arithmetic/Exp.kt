package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Exp(vararg e: Expr) : FunctionExpr(Function.EXP, *e) {

    override fun evaluate(): Expr {
        return Power(E(), get(0)).eval()
    }

    override fun validate() {
        validateParameterCount(1)
    }

    override val properties: Int
        get() = Properties.LISTABLE.value
}