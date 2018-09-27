package org.cerion.symcalc.expression

class BoolExpr(value: Boolean) : Expr() {

    companion object {
        @JvmField val TRUE = BoolExpr(true)
        @JvmField val FALSE = BoolExpr(false)
    }

    init {
        setValue(value)
    }

    fun value(): Boolean  = this.value as Boolean

    override fun equals(e: Expr): Boolean {
        if (e.isBool)
            return e.asBool().value() == value()

        return false
    }

    override fun toString(): String = if (value()) "True" else "False"
    override fun show(i: Int) = indent(i, "BoolExpr = " + toString())
    override fun evaluate(): Expr = this
    override fun getType(): Expr.ExprType = Expr.ExprType.BOOL
}
