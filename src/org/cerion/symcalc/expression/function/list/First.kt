package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.ListExpr

class First(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.LAST, *e) {

    override fun evaluate(): Expr {
        if (get(0).isList) {
            return get(0).asList().get(0)
        }

        return this
    }
}
