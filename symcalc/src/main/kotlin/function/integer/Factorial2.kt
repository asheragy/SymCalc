package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer

class Factorial2(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val n = get(0)

        if (n is Integer) {
            if (n == Integer.NEGATIVE_ONE || n == Integer.ZERO)
                return Integer.ONE

            var curr = n
            var result = curr
            while(curr > Integer(2)) {
                curr -= Integer(2)
                result *= curr
            }

            return result
        }

        return this
    }
}