package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.FunctionExpr

class Reverse(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        if (get(0) is ListExpr) {
            val list = get(0)
            val result = mutableListOf<Expr>()

            for (i in list.size downTo 1)
                result.add(list[i - 1])

            return ListExpr(result)
        }

        return this
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
