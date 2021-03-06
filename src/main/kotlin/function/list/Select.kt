package org.cerion.symcalc.function.list

import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr

class Select(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val list = this[0].asList()
        val result = mutableListOf<Expr>()

        for (i in 0 until list.size) {
            val f = (args[1] as SymbolExpr)
            val e = createFunction(f.value, list[i]).eval()

            if (e.equals(BoolExpr.TRUE)) {
                result.add(list[i])
            }
        }

        return ListExpr(result)
    }

    override fun validate() {
        validateParameterType(0, ExprType.LIST)
        validateParameterType(1, ExprType.SYMBOL)
    }
}
