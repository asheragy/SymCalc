package org.cerion.symcalc.expression

class VarExpr(override val value: String) : Expr() {

    override val type: ExprType
        get() = ExprType.VARIABLE

    override fun treeForm(i: Int) = indent(i, "VarExpr = $value")
    override fun toString(): String = value

    override fun evaluate(): Expr {
        var result = getEnvVar(value)
        if (result == null)
            result = this

        return result
    }

    override fun equals(e: Expr): Boolean {
        if (e.isVariable) {
            val t = e as VarExpr
            if (value.contentEquals(t.value))
                return true
        }

        return false
    }
}