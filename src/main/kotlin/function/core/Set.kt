package org.cerion.symcalc.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.FunctionExpr

class Set(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val v = get(0) as VarExpr
        val e = get(1)

        env.setVar(v.value, e)

        return e
    }

    override fun validate() {
        validateParameterCount(2)
        validateParameterType(0, ExprType.VARIABLE)
    }
}
