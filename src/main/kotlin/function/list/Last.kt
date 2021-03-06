package org.cerion.symcalc.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr

class Last(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        if (get(0) is ListExpr) {
            val l = get(0) as ListExpr
            return l[l.size - 1]
        }

        return this
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
