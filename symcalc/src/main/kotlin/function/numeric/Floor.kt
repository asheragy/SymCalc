package org.cerion.symcalc.function.numeric

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.NumberExpr

class Floor(vararg e: Expr) : FunctionExpr(*e) {

    override fun validate() {
        validateParameterCount(1)
    }

    override fun evaluate(): Expr {
        val x = get(0)

        if (x is NumberExpr)
            return x.floor()

        return this
    }
}