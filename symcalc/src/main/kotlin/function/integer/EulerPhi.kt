package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.list.Tally
import org.cerion.symcalc.number.Integer

class EulerPhi(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {

        val n = get(0)
        if (n is Integer) {
            if (n == Integer.ONE)
                return n

            val factors = Factor(n).eval() as ListExpr

            if (factors.size == 1)
                return n - 1

            // Use integer formula to calculate
            val tally = Tally(factors).eval() as ListExpr
            val terms = tally.map { it as ListExpr
                val prime = it[0] as Integer
                val count = it[1] as Integer

                prime.pow(count - 1) * (prime - 1)
            }

            return Times(*terms.args).eval()
        }

        return this
    }
}