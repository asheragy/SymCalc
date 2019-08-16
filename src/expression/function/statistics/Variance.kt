package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.number.Integer

class Variance(vararg e: Expr) : FunctionExpr(Function.VARIANCE, *e) {

    override fun evaluate(): Expr {
        val list = get(0) as ListExpr
        val mean = Mean(list).eval()
        val sum = Plus()

        for (i in 0 until list.size) {
            val e = list[i]
            val square = Power(Subtract(e, mean), Integer.TWO).eval() // (a - b)^2
            sum.add(square)
        }

        val result = Divide(sum, Integer(list.size - 1))

        return result.eval()
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
