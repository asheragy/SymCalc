package org.cerion.symcalc.function.matrix

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr

class VectorQ(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        if (get(0) !is ListExpr)
            return BoolExpr.FALSE

        val list = getList(0)
        for (i in 0 until list.size) {
            if (list[i] is ListExpr)
                return BoolExpr.FALSE
        }

        return BoolExpr.TRUE
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}