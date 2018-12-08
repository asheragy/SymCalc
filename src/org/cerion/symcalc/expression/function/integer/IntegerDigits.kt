package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.*
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.NumberType

class IntegerDigits(vararg e: Expr) : FunctionExpr(Function.INTEGER_DIGITS, *e) {

    public override fun evaluate(): Expr {
        val num = get(0).asInteger()
        val list = ListExpr()

        val s = num.toString()
        for (i in 0 until s.length)
            list.add(NumberExpr.parse("" + s[i]))

        return list
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberType.INTEGER)
    }
}
