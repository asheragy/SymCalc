package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.IntegerNum

class N(vararg e: Expr) : FunctionExpr(Function.N, *e) {

    override fun evaluate(): Expr {
        if (size() > 0) {
            if (size() > 1 && get(1).isInteger) {
                val n = get(1) as IntegerNum
                env.setNumericalEval(true, n.intValue())
            } else
                env.isNumericalEval = true

            return get(0).eval()
        }

        return this
    }
}
