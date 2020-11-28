package org.cerion.symcalc.expression

class ErrorExpr(override val value: String) : Expr(), AtomExpr {

    override val type: ExprType
        get() = ExprType.ERROR

    override fun toString(): String = "Error($value)"

    // There should never be a reason to compare errors so just say they are never equal
    override fun equals(e: Expr): Boolean = false
}
