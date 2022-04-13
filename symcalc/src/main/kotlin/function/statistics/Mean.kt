package org.cerion.symcalc.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.number.Integer

class Mean(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {

        val list = get(0) as ListExpr
        var result: Expr = Plus(*list.args)
        result = Divide(result, Integer(list.size))

        return result.eval()
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
