package org.cerion.symcalc.expression

class BoolExpr(value: Boolean) : Expr() {

    override val type: ExprType
        get() = ExprType.BOOL

    companion object {
        @JvmField val TRUE = BoolExpr(true)
        @JvmField val FALSE = BoolExpr(false)
    }

    init {
        this.value = value
    }

    fun value(): Boolean  = this.value as Boolean

    override fun equals(e: Expr): Boolean {
        if (e.isBool)
            return e.asBool().value() == value()

        return false
    }

    override fun toString(): String = if (value()) "True" else "False"
    override fun treeForm(i: Int) = indent(i, "BoolExpr = " + toString())
    override fun evaluate(): Expr = this
}
