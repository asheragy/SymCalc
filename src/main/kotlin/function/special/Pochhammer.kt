package org.cerion.symcalc.function.special

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr

class Pochhammer(vararg e: Expr) : FunctionExpr(*e) {
    override fun evaluate(): Expr {
        TODO("Not yet implemented")
    }


}