package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr

class Last(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.LAST, *e) {

    override fun evaluate(): Expr {
        if (get(0).isList) {
            val l = get(0).asList()
            return l[l.size() - 1]
        }

        return this
    }
}
