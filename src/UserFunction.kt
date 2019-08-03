package org.cerion.symcalc

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr

// TODO is name required? Anonymous functions should work, maybe UserFunction is just an expression...
class UserFunction(private val name: String, private val expr: Expr, vararg vars: VarExpr) {

    private val vars: List<VarExpr> = vars.asList()

    fun eval(vararg params: Expr): Expr {

        if (params.size != vars.size)
            throw IllegalArgumentException()

        for (i in vars.indices) {
            expr.env.setVar(vars[i].value, params[i])
        }

        return expr.eval()
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
