package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.combinatorial.Binomial
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr

class EulerE(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val n = get(0)
        if (n is Integer) {
            if (n.isOdd)
                return Integer.ZERO

            val t = (n / Integer(2)) as Integer
            return iteratedSum(t.intValue())
        }

        return this
    }

    // FEAT if a series of euler numbers is needed a more efficient algorithm can get them recursively

    // Double sum method of calculating E_2n
    private fun iteratedSum(n: Int): Integer {
        var sum = Integer.ZERO

        for(k in 1..(2*n + 1) step 2) {
            var innerSum: NumberExpr = Integer.ZERO
            val binomial = Binomial(k).eval() as ListExpr
            for(j in 0..k) {
                var numer = binomial[j] as NumberExpr
                numer *= Integer(k - 2*j).pow(2*n + 1)
                numer /= Integer.TWO.pow(k)
                numer /= Integer(k)

                // This and step 2 removes need for complex numbers in sum
                if (k % 4 == 3)
                    numer *= Integer.NEGATIVE_ONE

                if (j % 2 == 0)
                    innerSum += numer
                else
                    innerSum -= numer
            }

            sum += innerSum as Integer
            if (k % 2 == 0)
                sum += innerSum
        }

        return sum
    }
}