package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberType

class Binomial(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val n = get(0) as Integer
        val k = get(1) as Integer

        // n! / k!(n-k)!
        val n1 = Factorial(n).eval().asInteger()
        var n2 = n - k
        n2 = Factorial(n2).eval().asInteger()
        val n3 = Factorial(k).eval().asInteger()
        n2 = n2.times(n3)

        return n1 / n2
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(2)
        validateNumberType(0, NumberType.INTEGER)
        validateNumberType(1, NumberType.INTEGER)
    }
}
