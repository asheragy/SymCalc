package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus

class Total(vararg e: Expr) : FunctionExpr(Function.TOTAL, *e) {

    override fun evaluate(): Expr {

        if (get(0).isList) {
            val e = get(0).asList()
            return Plus(*e.args()).eval()
        }

        return this
    }
}
