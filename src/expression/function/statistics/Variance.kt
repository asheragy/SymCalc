package org.cerion.symcalc.expression.function.statistics

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.IntegerNum

class Variance(vararg e: Expr) : FunctionExpr(Function.VARIANCE, *e) {

    override fun evaluate(): Expr {

        val list = get(0) as ListExpr
        val mean = Mean(list).eval()

        val sum = Plus()

        for (i in 0 until list.size()) {
            val e = list[i]
            val diff = Subtract(e, mean).eval() // TODO may be another function for (a - b)^2
            val square = Times(diff, diff).eval()
            sum.add(square)
        }

        val result = Divide(sum, IntegerNum(list.size() - 1))

        return result.eval()
    }
}
