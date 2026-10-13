package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr

class UserFunction(private val name: String, private val expr: Expr, vararg vars: VarExpr) {

    private val vars: List<VarExpr> = vars.asList()

    fun eval(vararg params: Expr): Expr {
        val ctx = EvalContext()

        if (params.size != vars.size)
            throw IllegalArgumentException()

        for (i in vars.indices) {
            ctx[vars[i].value] = params[i]
        }

        return expr.eval(ctx)
    }

    override fun toString(): String {

        var params = ""
        for (i in vars.indices) {
            if (i > 0)
                params += ","
            params += vars[i]
        }

        return "$name[$params] = $expr"
    }
}
