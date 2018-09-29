package org.cerion.symcalc.expression

class VarExpr(str: String) : Expr() {

    override val type: ExprType
        get() = ExprType.VARIABLE

    init {
        value = str
    }

    override fun show(i: Int) = indent(i, "VarExpr = " + value())
    override fun toString(): String = value()

    fun value(): String {
        return value as String
    }

    override fun evaluate(): Expr {
        var result = env.getVar(value())
        if (result == null)
            result = this

        return result
    }

    override fun equals(e: Expr): Boolean {
        if (e.isVariable) {
            val t = e as VarExpr
            if (value().contentEquals(t.value()))
                return true
        }

        return false
    }
}