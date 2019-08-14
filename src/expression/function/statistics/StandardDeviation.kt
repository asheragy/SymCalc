package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Sqrt

class StandardDeviation(vararg e: Expr) : FunctionExpr(Function.STANDARD_DEVIATION, *e) {

    override fun evaluate(): Expr {
        val list = get(0).asList()
        val variance = Variance(list)

        return Sqrt(variance).eval()
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
