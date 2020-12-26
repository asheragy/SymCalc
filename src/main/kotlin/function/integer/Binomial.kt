package org.cerion.symcalc.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberType

class Binomial(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val n = get(0) as Integer

        // n! / k!(n-k)!
        val nfact = Factorial(n).eval().asInteger()

        if (size > 1) {
            val k = get(1) as Integer

            var n2 = n - k
            n2 = Factorial(n2).eval().asInteger()
            val n3 = Factorial(k).eval().asInteger()
            n2 = n2.times(n3)

            return nfact / n2
        }

        // When only 1 value is provided, return range of 0..n
        val result = mutableListOf(Integer(1))
        var k_fact = Integer.ONE
        var nk_fact = nfact

        for (k in 1..n.intValue() / 2) {
            k_fact *= Integer(k)
            nk_fact = (nk_fact / Integer(n.intValue() - k + 1)) as Integer

            val t = (nfact / (k_fact * nk_fact))
            result.add(t as Integer)
        }

        for(k in (n.intValue() / 2) + 1..n.intValue()) {
            result.add(result[n.intValue() - k])
        }

        return ListExpr(result)
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterRange(1,2)
        validateNumberType(0, NumberType.INTEGER)
        if (size == 2)
            validateNumberType(1, NumberType.INTEGER)
    }
}
