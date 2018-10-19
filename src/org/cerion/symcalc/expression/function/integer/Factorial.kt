package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.number.NumberType
import org.cerion.symcalc.expression.number.IntegerNum

class Factorial(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.FACTORIAL, *e) {

    override fun evaluate(): Expr {
        //TODO can work on non-integer

        var result = IntegerNum.ONE
        var N = get(0).asInteger()
        while (N > IntegerNum.ONE) {
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
