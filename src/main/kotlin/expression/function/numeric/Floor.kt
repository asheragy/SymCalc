package org.cerion.symcalc.expression.function.numeric

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr

class Floor(vararg e: Expr) : FunctionExpr(*e) {

    // FEAT 2nd parameter for multiple

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