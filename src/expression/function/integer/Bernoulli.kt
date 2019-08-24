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
        val result: Expr
        var N = get(0) as Integer

        when {
            N.isZero -> result = Integer.ONE
            N.isOne -> result = Rational(-1,2)
            else -> {
                var res: NumberExpr = Integer.ZERO
                val n = N.intValue()


                for (i in 0 until n) {
                    val bin = Binomial(Integer(n + 1), Integer(i))
                    val t = bin.eval() as Integer

                    val bern = Bernoulli(Integer(i))
                    var b = bern.eval().asNumber()

                    b = t * b
                    res+= b
                }

                res = -res
                N+= Integer.ONE
                res/= N

                result = res
            }
        }

        return result
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberType.INTEGER)
    }
}