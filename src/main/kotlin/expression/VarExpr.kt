package org.cerion.symcalc.expression

class VarExpr(override val value: String) : Expr(), AtomExpr {

    override val type: ExprType
        get() = ExprType.VARIABLE

    override fun toString(): String = value

    override fun eval(): Expr {
        var result = getEnvVar(value)
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