package org.cerion.symcalc.function.combinatorial

import org.cerion.math.bignum.factorial
import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberType

class Factorial(vararg e: Any) : FunctionExpr(*e) {

    // FEAT can work with non-integer values but needs Gamma function first

    override fun evaluate(): Expr {
        val n = get(0) as Integer
        return Integer(factorial(n.intValue()))
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateNumberType(0, NumberType.INTEGER)
    }
}
