package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberType
import org.cerion.symcalc.expression.number.Integer

class Factorial(vararg e: Expr) : FunctionExpr(Function.FACTORIAL, *e) {

    override fun evaluate(): Expr {
        var result = Integer.ONE
        var N = get(0) as Integer

        while (N > Integer.ONE) {
            result*= N
            N--
        }

        return result
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateNumberType(0, NumberType.INTEGER)
    }
}
