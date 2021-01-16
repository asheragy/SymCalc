package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer

class Divisors (vararg e: Any) : FunctionExpr(*e) {
    override fun evaluate(): Expr {
        val n = get(0) as Integer
        val factors = Factor(n).eval() as ListExpr

        if (factors.size == 1)
            return ListExpr(1, n)

        val result = factors.args.map { it as Integer }.toMutableList()

        for(m in factors.args.distinct()) {
            val t = Divisors(n / m).eval() as ListExpr
            result.addAll(t.args.map { it as Integer })
        }

        result.add(1, n)
        return ListExpr(*result.distinct().sorted().toTypedArray())
    }
}