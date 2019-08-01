package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Select(vararg e: Expr) : FunctionExpr(Function.SELECT, *e) {

    override fun evaluate(): Expr {
        val list = this[0].asList().eval()
        val result = ListExpr()

        for (i in 0 until list.size) {
            val f = (args[1] as FunctionExpr)
            val e = createFunction(f.name, list[i]).eval()

            if (e.equals(BoolExpr.TRUE)) {
                result.add(list[i])
            }
        }

        return result
    }

    override fun validate() {
        validateParameterType(0, ExprType.LIST)
        validateParameterType(1, ExprType.FUNCTION)
    }
}
