package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberType

class PrimeQ(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val n = get(0) as Integer

        return if (n.value.isProbablePrime(5))
            BoolExpr.TRUE
        else
            BoolExpr.FALSE
    }

    override fun validate() {
        validateNumberType(0, NumberType.INTEGER)
    }
}

