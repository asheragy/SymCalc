package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.NumberType

class PrimeQ(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        return if (get(0).asInteger().primeQ())
            BoolExpr.TRUE
        else
            BoolExpr.FALSE
    }

    override fun validate() {
        validateNumberType(0, NumberType.INTEGER)
    }
}

