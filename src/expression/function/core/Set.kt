package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Set(vararg e: Expr) : FunctionExpr(Function.SET, *e) {

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
