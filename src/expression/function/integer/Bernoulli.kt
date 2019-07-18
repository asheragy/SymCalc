package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.NumberType
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.RationalNum

class Bernoulli(vararg e: Expr) : FunctionExpr(Function.BERNOULLI, *e) {

    override fun evaluate(): Expr {

        val result: Expr
        var N = get(0).asInteger()

        when {
            N.isZero -> result = IntegerNum.ONE
            N.isOne -> result = RationalNum(-1,2)
            else -> {
                var res: NumberExpr = IntegerNum.ZERO
                val n = N.intValue()


                for (i in 0 until n) {
                    val bin = Binomial(IntegerNum(n + 1), IntegerNum(i))
                    val t = bin.eval().asInteger()

                    val bern = Bernoulli(IntegerNum(i))
                    var b = bern.eval().asNumber()

                    b = t * b
                    res+= b
                }

                res = -res
                N+= IntegerNum.ONE
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