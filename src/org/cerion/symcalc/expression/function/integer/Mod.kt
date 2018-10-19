package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.number.NumberType

class Mod(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.MOD, *e) {

    override fun evaluate(): Expr {
        //TODO can work on non-integers

        val a = get(0).asInteger()
        val b = get(1).asInteger()

        return a.mod(b)
    }

    override fun validate() {
        validateParameterCount(2)
        validateNumberType(0, NumberType.INTEGER)
        validateNumberType(1, NumberType.INTEGER)
    }
}
