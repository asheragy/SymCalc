package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.*
import org.cerion.symcalc.expression.number.NumberType

class PrimeQ(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.PRIMEQ, *e) {

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

