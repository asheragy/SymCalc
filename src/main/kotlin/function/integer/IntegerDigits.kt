package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.NumberType

class IntegerDigits(vararg e: Expr) : FunctionExpr(*e) {

    public override fun evaluate(): Expr {
        val num = get(0) as Integer
        val s = num.toString()

        return ListExpr(s.map { NumberExpr.parse("" + it) })
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberType.INTEGER)
    }
}
