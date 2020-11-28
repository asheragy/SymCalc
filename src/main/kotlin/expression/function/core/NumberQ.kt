package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr

class NumberQ(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        return BoolExpr(get(0) is NumberExpr)
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
