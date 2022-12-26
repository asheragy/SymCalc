package org.cerion.symcalc.function.calculus

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Plus

class Sum(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = get(0)
        val interval = get(1) as ListExpr
        val i = interval[0] as VarExpr
        val min = interval[1] as Integer
        val max = interval[2] as Integer

        return evaluate(e, i, min, max)
    }

    private fun evaluate(expr: Expr, i: VarExpr, min: Integer, max: Integer): Expr {
        val range = (min.intValue()..max.intValue()).map { it }
        val values = range.map {
            setEnvVar(i.value, Integer(it))
            expr.eval()
        }

        val sum = Plus(*values.toTypedArray())
        return sum.eval()
    }

    override val properties: Int
        get() = Properties.HoldAll.value
}