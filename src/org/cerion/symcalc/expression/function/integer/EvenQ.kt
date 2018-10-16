package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr

class EvenQ(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.EVENQ, *e) {

    override fun evaluate(): Expr {
        if (!get(0).isInteger)
            return BoolExpr.FALSE

        return if (get(0).asInteger().isEven) BoolExpr.TRUE else BoolExpr.FALSE
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
