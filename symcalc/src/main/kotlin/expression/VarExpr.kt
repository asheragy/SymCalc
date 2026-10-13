package org.cerion.symcalc.expression

import org.cerion.symcalc.EvalContext

class VarExpr(override val value: String) : Expr(), AtomExpr {

    override val type: ExprType
        get() = ExprType.VARIABLE

    override fun toString(): String = value

    override fun eval(ctx: EvalContext): Expr {
        var result = ctx.lookup(value)
        if (result == null)
            result = this

        return result
    }

    override fun equals(e: Expr): Boolean {
        if (e is VarExpr) {
            if (value.contentEquals(e.value))
                return true
        }

        return false
    }
}
