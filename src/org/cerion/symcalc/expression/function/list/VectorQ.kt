package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class VectorQ(vararg e: Expr) : FunctionExpr(Function.VECTORQ, *e) {

    override fun evaluate(): Expr {
        if (!get(0).isList)
            return BoolExpr.FALSE

        val list = getList(0)
        for (i in 0 until list.size()) {
            if (list[i].isList)
                return BoolExpr.FALSE
        }

        return BoolExpr.TRUE
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}