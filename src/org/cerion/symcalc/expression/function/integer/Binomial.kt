package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.NumberExpr
import org.cerion.symcalc.expression.number.IntegerNum

class Binomial(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.BINOMIAL, *e) {

    override fun evaluate(): Expr {
        val n = get(0).asInteger()
        val k = get(1).asInteger()

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
        validateNumberType(0, NumberExpr.INTEGER)
        validateNumberType(1, NumberExpr.INTEGER)
    }
}
