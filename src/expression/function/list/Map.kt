package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Map(vararg e: Expr) : FunctionExpr(Function.MAP, *e) {

    override fun evaluate(): Expr {
        val f = args[0] as FunctionExpr
        val list = args[1].eval() as ListExpr
        val result = ListExpr()

        for (i in 0 until list.size) {
            val e = createFunction(f.name, list[i])
            result.add(e.eval())
        }

        return result
    }

    override fun validate() {
        validateParameterType(0, ExprType.FUNCTION)
        validateParameterType(1, ExprType.LIST)
    }
}
