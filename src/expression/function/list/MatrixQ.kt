package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class MatrixQ(vararg e: Expr) : FunctionExpr(Function.MATRIXQ, *e) {

    override fun evaluate(): Expr {
        if (get(0) !is ListExpr)
            return BoolExpr.FALSE

        var size = -1
        val list = getList(0)
        for (i in 0 until list.size) {
            // Every sublist must be a vector of the same length
            if (list[i] !is ListExpr)
                return BoolExpr.FALSE

            val sublist = list.getList(i)
            if (VectorQ(sublist).eval() !== BoolExpr.TRUE)
                return BoolExpr.FALSE

            if (size > 0) {
                if (sublist.size != size)
                    return BoolExpr.FALSE
            } else
                size = sublist.size
        }

        return BoolExpr.TRUE
    }

    override fun validate() {
    }
}
