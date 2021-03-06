package org.cerion.symcalc.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr

class Join(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val items = mutableListOf<Expr>()

        for (i in 0 until size) {
            val e = get(i) as ListExpr
            items.addAll(e.args)
        }

        return ListExpr(*items.toTypedArray())
    }

    @Throws(ValidationException::class)
    override fun validate() {
        for (i in 0 until size)
            validateParameterType(i, ExprType.LIST)
    }
}
