package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr

class First(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.LAST, *e) {

    override fun evaluate(): Expr {
        if (get(0).isList) {
            return get(0).asList()[0]
        }

        return this
    }
}
