package org.cerion.symcalc.expression.function.procedural

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class If(vararg e: Expr) : FunctionExpr(Function.IF, *e) {

    override fun evaluate(): Expr {
        val p0 = get(0)
        return if (p0.isBool) {
            if (p0.asBool() === BoolExpr.TRUE) get(1) else get(2)

        } else this

    }
}
