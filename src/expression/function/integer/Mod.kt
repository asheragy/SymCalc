package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberType

class Mod(vararg e: Expr) : FunctionExpr(Function.MOD, *e) {

    override fun evaluate(): Expr {
        val a = get(0).asInteger()
        val b = get(1).asInteger()

        return a.rem(b)
    }

    override fun validate() {
        validateParameterCount(2)
        validateNumberType(0, NumberType.INTEGER)
        validateNumberType(1, NumberType.INTEGER)
    }
}
