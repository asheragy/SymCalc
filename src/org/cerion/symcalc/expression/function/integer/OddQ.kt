package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class OddQ(vararg e: Expr) : FunctionExpr(Function.ODDQ, *e) {

    override fun evaluate(): Expr {
        if (!get(0).isInteger)
            return BoolExpr.FALSE

        return if (get(0).asInteger().isOdd) BoolExpr.TRUE else BoolExpr.FALSE
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
