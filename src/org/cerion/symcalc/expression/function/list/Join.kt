package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.ListExpr

class Join(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.JOIN, e) {

    override fun evaluate(): Expr {
        val result = ListExpr()

        for (i in 0 until size()) {
            val e = get(i) as ListExpr
            result.addAll(e.all)
        }

        return result
    }

    @Throws(ValidationException::class)
    override fun validate() {
        for (i in 0 until size())
            validateParameterType(i, Expr.ExprType.LIST)
    }
}
