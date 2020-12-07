package org.cerion.symcalc.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberType

class Factorial(vararg e: Expr) : FunctionExpr(*e) {

    // FEAT can work with non-integer values but needs Gamma function first

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
