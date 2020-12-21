package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.NumberType
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.function.FunctionExpr

class Bernoulli(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = get(0)
        if (e is Integer)
            return bernoulli(e.intValue())

        return this
    }

    private fun bernoulli(n: Int): NumberExpr {
        when(n) {
            0 -> return Integer.ONE
            1 -> return Rational(-1,2)
            else -> {
                var res: NumberExpr = Integer.ZERO

                val values = mutableListOf<NumberExpr>()
                val binomial = Binomial(Integer(n + 1)).eval() as ListExpr
                for (i in 0 until n) {
                    val bin = binomial[i]
                    val t = bin.eval() as Integer

                    val bern = bernoulliRecursive(i, values)
                    values.add(bern)
                    var b = bern.eval() as NumberExpr

                    b = t * b
                    res+= b
                }

                res = -res
                res/= Integer(n + 1)

                return res
            }
        }
    }

    private fun bernoulliRecursive(n: Int, previous: List<NumberExpr>): NumberExpr {
        when(n) {
            0 -> return Integer.ONE
            1 -> return Rational(-1,2)
            else -> {
                if (n % 2 == 1)
                    return Integer.ZERO

                var res: NumberExpr = Integer.ZERO

                val binomial = Binomial(Integer(n+1)).eval() as ListExpr
                for (i in 0 until n) {
                    val bin = binomial[i] as Integer

                    val bern = previous[i]
                    var b = bern.eval() as NumberExpr

                    b = bin * b
                    res+= b
                }

                res = -res
                res/= Integer(n + 1)

                return res
            }
        }
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberType.INTEGER)
    }
}