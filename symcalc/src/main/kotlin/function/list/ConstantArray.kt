package org.cerion.symcalc.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.NumberType

class ConstantArray(vararg e: Expr) : FunctionExpr(*e) {

    // TODO 2nd parameter can be N-dimensional

    override fun evaluate(): Expr {
        val expr = get(0)
        val count = get(1).asInteger()

        val list = mutableListOf<Expr>()
        for(i in 1..count.intValue())
            list.add(expr)

        return ListExpr(list)
    }

    override fun validate() {
        validateParameterCount(2)
        validateNumberType(1, NumberType.INTEGER)
    }
}