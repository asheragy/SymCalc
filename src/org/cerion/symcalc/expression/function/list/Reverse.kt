package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.ListExpr

class Reverse(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.REVERSE, *e) {

    override fun evaluate(): Expr {
        if (get(0).isList) {
            val list = get(0).asList()
            val result = ListExpr()

            for (i in list.size() downTo 1)
                result.add(list[i - 1])

            return result
        }

        return this
    }
}