package org.cerion.symcalc.expression

open class GraphicsExpr : Expr() {

    override val type: ExprType
        get() = ExprType.GRAPHICS

    override fun toString(): String = "Graphics"
    override fun equals(e: Expr): Boolean = false
}
