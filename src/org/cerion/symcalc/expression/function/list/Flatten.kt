package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.ListExpr

class Flatten(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.FLATTEN, e) {

    override fun evaluate(): Expr {
        val l = get(0) as ListExpr
        val result = ListExpr()

        for (i in 0 until l.size()) {
            val e = l.get(i)

            if (e.isList) {
                var sublist = e as ListExpr
                sublist = Flatten(sublist).eval() as ListExpr // TODO eval may be redundant since getAll will eval
                result.addAll(sublist.all)
            } else {
                result.add(e)
            }
        }

        return result
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterType(0, Expr.ExprType.LIST)
    }
}