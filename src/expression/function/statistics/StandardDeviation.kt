package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Sqrt
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.core.N
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RealNum

class StandardDeviation(vararg e: Expr) : FunctionExpr(Function.STANDARD_DEVIATION, *e) {

    // TODO add some other more advanced usages but ignoring for now, matrix and dist parameters

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
