package org.cerion.symcalc.function.list

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr

class First(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        return get(0).asList()[0]
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
