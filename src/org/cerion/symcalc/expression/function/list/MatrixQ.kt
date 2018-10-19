package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr

class MatrixQ(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.MATRIXQ, *e) {

    override fun evaluate(): Expr {
        if (!get(0).isList)
            return BoolExpr.FALSE

        var size = -1
        val list = getList(0)
        for (i in 0 until list.size()) {
            // Every sublist must be a vector of the same length
            if (!list[i].isList)
                return BoolExpr.FALSE

            val sublist = list.getList(i)
            if (VectorQ(sublist).eval() !== BoolExpr.TRUE)
                return BoolExpr.FALSE

            if (size > 0) {
                if (sublist.size() != size)
                    return BoolExpr.FALSE
            } else
                size = sublist.size()
        }

        return BoolExpr.TRUE
    }
}