package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.number.Integer

class DivisorSigma (vararg e: Any) : FunctionExpr(*e) {
    override fun evaluate(): Expr {
        val k = get(0) as Integer
        val n = get(1) as Integer

        val divisors = Divisors(n).eval() as ListExpr
        val values = divisors.args.map { Power(it, k).eval() }

        return values.reduce { acc, expr -> acc + expr }
    }
}