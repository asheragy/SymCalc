package org.cerion.symcalc.expression

import org.cerion.symcalc.expression.function.FunctionExpr

class SymbolExpr(override val value: String) : Expr(), AtomExpr {

    override val type: ExprType get() = ExprType.SYMBOL

    override fun toString(): String = "Symbol $value"
    override fun equals(e: Expr): Boolean = e is SymbolExpr && value == e.value

    override fun eval(): Expr {
        if(FunctionExpr.isValidFunction(value))
            return this

        return ErrorExpr("Invalid function name '$value'")
    }

    fun eval(vararg e: Expr): Expr {
        return FunctionExpr.createFunction(value, *e).eval()
    }
}