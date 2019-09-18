package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.number.Integer

class Variance(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val list = get(0) as ListExpr
        val mean = Mean(list).eval()

        // Sum of (a - b)^2
        val sum = Plus(*list.args.map { Power(Subtract(it, mean), Integer.TWO).eval() }.toTypedArray())

        val result = Divide(sum, Integer(list.size - 1))
        return result.eval()
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
