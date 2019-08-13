package expression

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionFactory

class SymbolExpr(override val value: String) : Expr() {

    override val type: ExprType get() = ExprType.SYMBOL

    override fun toString(): String = "Symbol $value"
    override fun treeForm(i: Int) = indent(i, "Symbol = $name")
    override fun equals(e: Expr): Boolean = e is SymbolExpr && value == e.value
    val name: String = value

    override fun evaluate(): Expr {
        val function = FunctionFactory.createInstance(name)
        return function
    }
}