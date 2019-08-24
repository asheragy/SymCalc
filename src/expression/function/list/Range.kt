package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr

// TODO_LP Implemented with just NumberExpr, this function works on numeric values such as Pi and Sqrt[2]
class Range(vararg e: Expr) : FunctionExpr(Function.RANGE, *e) {

    override fun evaluate(): Expr {
        if (size == 1)
            return range(Integer.ONE, get(0) as NumberExpr, Integer.ONE)

        if (size == 2)
            return range(get(0) as NumberExpr, get(1) as NumberExpr, Integer.ONE)

        return range(get(0) as NumberExpr, get(1) as NumberExpr, get(2) as NumberExpr)
    }

    private fun range(min: NumberExpr, max: NumberExpr, step: NumberExpr): ListExpr {
        val list = ListExpr()
        var current = min

        while(current <= max) {
            list.add(current)
            current += step
        }

        return list
    }

    override fun validate() {
        validateParameterType(0, ExprType.NUMBER)
        if (size > 1)
            validateParameterType(1, ExprType.NUMBER)
        else if (size > 2)
            validateParameterType(1, ExprType.NUMBER)

        validateParameterRage(1, 3)
    }
}
