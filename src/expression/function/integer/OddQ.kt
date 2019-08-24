package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer

class OddQ(vararg e: Expr) : FunctionExpr(Function.ODDQ, *e) {

    override fun evaluate(): Expr {
        val n = get(0)
        if (n is Integer)
            return BoolExpr(n.isOdd)

        return BoolExpr.FALSE
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
