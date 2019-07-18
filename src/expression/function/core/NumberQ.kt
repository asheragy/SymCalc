package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class NumberQ(vararg e: Expr) : FunctionExpr(Function.NUMBERQ, *e) {

    override fun evaluate(): Expr {
        return BoolExpr(get(0).isNumber)
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
