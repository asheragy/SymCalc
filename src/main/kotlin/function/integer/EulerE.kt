package org.cerion.symcalc.function.integer

import org.cerion.symcalc.constant.I
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Power
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
        var sum: Expr = Integer.ZERO

        for(k in 1..(2*n + 1) step 2) {

            var innerSum: Expr = Integer.ZERO
            val binomial = Binomial(k).eval() as ListExpr
            for(j in 0..k) {
                var numer: NumberExpr = if(j % 2 == 0) Integer.ONE else Integer.NEGATIVE_ONE
                numer *= Integer(k - 2*j).pow(2*n + 1)
                numer /= Integer.TWO.pow(k)
                numer /= Power(I(), k).eval() as NumberExpr
                numer /= Integer(k)

                innerSum += binomial[j] * numer
            }

            //println("$k $innerSum")

            sum += innerSum
            if (k % 2 == 0)
                sum += innerSum
        }

        return (I() * sum) as Integer
    }
}