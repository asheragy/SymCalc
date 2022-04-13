package org.cerion.symcalc.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Plus

class Total(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {

        if (get(0) is ListExpr) {
            val e = get(0) as ListExpr
            return Plus(*e.args).eval()
        }

        return this
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
