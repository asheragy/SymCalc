package org.cerion.symcalc.expression.function.list

import expression.SymbolExpr
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Map(vararg e: Expr) : FunctionExpr(Function.MAP, *e) {

    // TODO this is a workaround until 1st parameter can be passed in differently for function copy
    override val properties: Int
        get() = Expr.Properties.HOLD.value

    override fun evaluate(): Expr {
        val symbol = args[0] as SymbolExpr
        val list = args[1].eval() as ListExpr
        val result = ListExpr()

        for (i in 0 until list.size) {
            val e = createFunction(symbol.name, list[i])
            result.add(e.eval())
        }

        return result
    }

    override fun validate() {
        validateParameterType(0, ExprType.SYMBOL)
        validateParameterType(1, ExprType.LIST)
    }
}
