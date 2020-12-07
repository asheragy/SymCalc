package org.cerion.symcalc.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer

class EvenQ(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val n = get(0)
        if (n is Integer) {
            return BoolExpr(n.isEven)
        }

        return BoolExpr.FALSE
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
