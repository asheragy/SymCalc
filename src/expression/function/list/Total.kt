package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus

class Total(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {

        if (get(0) is ListExpr) {
            val e = get(0) as ListExpr
            return Plus(*e.args.toTypedArray()).eval()
        }

        return this
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
