package org.cerion.symcalc.function.logical

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.NumberExpr

class Equal(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        if (size == 1)
            return BoolExpr.TRUE

        val a = get(0)
        val b = get(1)
        if (a is NumberExpr && b is NumberExpr) {
            return BoolExpr(a.compareTo(b) == 0)
        }

        return this
    }

    override fun validate() {
    }
}
