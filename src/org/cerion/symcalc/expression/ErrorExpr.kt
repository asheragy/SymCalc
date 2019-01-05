package org.cerion.symcalc.expression

class ErrorExpr(error: String) : Expr() {

    override val type: ExprType
        get() = ExprType.ERROR

    init {
        value = error
    }

    override fun treeForm(i: Int) = indent(i, "Error " + this.toString())
    override fun evaluate(): Expr = this
    override fun toString(): String = "Error($value)"

    // There should never be a reason to compare errors so just say they are never equal
    override fun equals(e: Expr): Boolean = false
}
