package org.cerion.symcalc.function.procedural

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr

class If(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val p0 = get(0)
        return if (p0 is BoolExpr) {
            if (p0 === BoolExpr.TRUE) get(1) else get(2)
        }
        else this

    }

    override fun validate() {
    }
}
