package org.cerion.symcalc.expression

class ErrorExpr(error: String) : Expr() {

    init {
        value = error
    }

    override fun show(i: Int) = indent(i, "Error " + this.toString())
    override fun evaluate(): Expr = this
    override fun getType(): Expr.ExprType = Expr.ExprType.ERROR
    override fun toString(): String = "Error($value)"

    // There should never be a reason to compare errors so just say they are never equal
    override fun equals(e: Expr): Boolean = false
}
