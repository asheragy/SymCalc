package org.cerion.symcalc.function.combinatorial

import org.cerion.math.bignum.binomial
import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberType

class Binomial(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        // TODO this can work with Rational too

        val n = get(0) as Integer

        if (size > 1) {
            val k = get(1) as Integer
            return Integer(binomial(n.value.intValueExact(), k.value.intValueExact()))
        }

        return ListExpr(binomial(n.value.intValueExact()).map { Integer(it) })
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterRange(1,2)
        validateNumberType(0, NumberType.INTEGER)
        if (size == 2)
            validateNumberType(1, NumberType.INTEGER)
    }
}
