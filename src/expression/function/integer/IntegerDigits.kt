package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.*
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.NumberType

class IntegerDigits(vararg e: Expr) : FunctionExpr(Function.INTEGER_DIGITS, *e) {

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
