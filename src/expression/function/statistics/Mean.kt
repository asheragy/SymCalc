package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.Integer

class Mean(vararg e: Expr) : FunctionExpr(Function.MEAN, *e) {

    override fun evaluate(): Expr {

        val list = get(0) as ListExpr
        var result: Expr = Plus(*list.args.toTypedArray())
        result = Divide(result, Integer(list.size))

        return result.eval()
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
