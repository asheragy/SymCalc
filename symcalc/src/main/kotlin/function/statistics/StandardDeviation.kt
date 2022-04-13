package org.cerion.symcalc.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Sqrt

class StandardDeviation(vararg e: Expr) : FunctionExpr(*e) {

    // FEAT Has more advanced usages, matrix/dist parameters

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
