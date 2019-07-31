package org.cerion.symcalc.expression.function.list

import expression.SymbolExpr
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Select(vararg e: Expr) : FunctionExpr(Function.SELECT, *e) {

    // TODO this is a workaround until 2nd parameter can be passed in differently for function copy
    override val properties: Int
        get() = Expr.Properties.HOLD.value

    override fun evaluate(): Expr {
        val list = this[0].asList().eval()
        val result = ListExpr()

        for (i in 0 until list.size) {
            val s = (args[1] as SymbolExpr)
            val e = createFunction(s.name, list[i]).eval()

            if (e.equals(BoolExpr.TRUE)) {
                result.add(list[i])
            }
        }

        return result
    }

    override fun validate() {
        validateParameterType(0, ExprType.LIST)
        validateParameterType(1, ExprType.SYMBOL)
    }
}
