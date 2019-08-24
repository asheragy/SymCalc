package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function

class Flatten(vararg e: Expr) : FunctionExpr(Function.FLATTEN, *e) {

    override fun evaluate(): Expr {
        val l = get(0) as ListExpr
        val items = mutableListOf<Expr>()

        for (i in 0 until l.size) {
            val e = l[i]

            if (e is ListExpr) {
                val sublist = Flatten(e).eval() as ListExpr
                items.addAll(sublist.args)
            } else {
                items.add(e)
            }
        }

        return ListExpr(*items.toTypedArray())
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterType(0, ExprType.LIST)
    }
}