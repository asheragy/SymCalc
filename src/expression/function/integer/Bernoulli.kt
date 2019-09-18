package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.NumberType
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational

class Bernoulli(vararg e: Expr) : FunctionExpr(Function.BERNOULLI, *e) {

    override fun evaluate(): Expr {
        val N = get(0) as Integer
        return bernoulli(N.intValue())
    }

    private fun bernoulli(n: Int): NumberExpr {
        when(n) {
            0 -> return Integer.ONE
            1 -> return Rational(-1,2)
            else -> {
                var res: NumberExpr = Integer.ZERO

                val values = mutableListOf<NumberExpr>()
                for (i in 0 until n) {
                    val bin = Binomial(Integer(n + 1), Integer(i))
                    val t = bin.eval() as Integer

                    val bern = bernoulliRecursive(i, values)
                    values.add(bern)
                    var b = bern.eval().asNumber()

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
                var res: NumberExpr = Integer.ZERO

                for (i in 0 until n) {
                    val bin = Binomial(Integer(n + 1), Integer(i))
                    val t = bin.eval() as Integer

                    val bern = previous[i]
                    var b = bern.eval().asNumber()

                    b = t * b
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