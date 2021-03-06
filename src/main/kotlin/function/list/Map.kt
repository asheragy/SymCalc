package org.cerion.symcalc.function.list

import org.cerion.symcalc.expression.SymbolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr

class Map(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val f = args[0] as SymbolExpr
        val list = args[1].eval() as ListExpr

        return ListExpr(list.args.map { createFunction(f.value, it).eval() })
    }

    override fun validate() {
        validateParameterType(0, ExprType.SYMBOL)
        validateParameterType(1, ExprType.LIST)
    }
}
