package org.cerion.symcalc.expression

class BoolExpr(override val value: Boolean) : Expr() {

    override val type: ExprType
        get() = ExprType.BOOL

    companion object {
        @JvmField val TRUE = BoolExpr(true)
        @JvmField val FALSE = BoolExpr(false)
    }

    override fun equals(e: Expr): Boolean {
        if (e is BoolExpr)
            return e.value == value

        return false
    }

    override fun toString(): String = if (value) "True" else "False"
    override fun treeForm(i: Int) = indent(i, "BoolExpr = " + toString())
    override fun evaluate(): Expr = this
}
