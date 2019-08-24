package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class Map(vararg e: Expr) : FunctionExpr(Function.MAP, *e) {

    override fun evaluate(): Expr {
        val f = args[0] as FunctionExpr
        val list = args[1].eval() as ListExpr

        return ListExpr(list.args.map { createFunction(f.name, it).eval() })
    }

    override fun validate() {
        validateParameterType(0, ExprType.FUNCTION)
        validateParameterType(1, ExprType.LIST)
    }
}
