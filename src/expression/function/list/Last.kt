package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Last(vararg e: Expr) : FunctionExpr(Function.LAST, *e) {

    override fun evaluate(): Expr {
        if (get(0) is ListExpr) {
            val l = get(0)
            return l[l.size - 1]
        }

        return this
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
