package org.cerion.symcalc.function.special

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.function.FunctionExpr

class Pochhammer(vararg e: Any) : FunctionExpr(*e) {
    override fun evaluate(): Expr {
        val x = get(0)
        val n = get(1)

        if (x is NumberExpr && n is Integer) {
            if (n.isZero)
                return Integer.ONE

            var result = x
            for (i in 1 until n.intValue()) {
                result *= x + Integer(i)
            }

            return result
        }

        return this
    }
}