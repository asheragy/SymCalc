package expression

import org.cerion.symcalc.expression.AtomExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionFactory

class SymbolExpr(override val value: String) : Expr(), AtomExpr {

    override val type: ExprType get() = ExprType.SYMBOL

    override fun toString(): String = "Symbol $value"
    override fun equals(e: Expr): Boolean = e is SymbolExpr && value == e.value
    val name: String = value

    override fun evaluate(): Expr {
        return FunctionFactory.createInstance(name)
    }
}