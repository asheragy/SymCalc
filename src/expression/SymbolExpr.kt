package expression

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionFactory

class SymbolExpr(str: String) : Expr() {

    override val type: ExprType get() = ExprType.SYMBOL

    init {
        value = str
    }

    override fun toString(): String = "Symbol $value"
    override fun treeForm(i: Int) = indent(i, "Symbol = $name")
    override fun equals(e: Expr): Boolean = e is SymbolExpr && value == e.value
    val name: String = value as String

    override fun evaluate(): Expr {
        // TODO add variable lookup from env

        val function = FunctionFactory.createInstance(name)
        return function
    }
}