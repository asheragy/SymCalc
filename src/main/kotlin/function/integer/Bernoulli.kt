package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.NumberType
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.function.FunctionExpr

class Bernoulli(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = get(0)
        if (e is Integer)
            return bernoulli(e.intValue()).first

        return this
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberType.INTEGER)
    }

    companion object {
        fun list(n: Int): List<NumberExpr> = bernoulli(n).second

        private fun bernoulli(n: Int): Pair<NumberExpr, List<NumberExpr>> {
            when(n) {
                0 -> return Pair(Integer.ONE, emptyList())
                1 -> return Pair(Rational(-1,2), emptyList())
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

                    return Pair(res, values)
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

    }
}