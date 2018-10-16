package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.NumberExpr
import org.cerion.symcalc.expression.number.IntegerNum

class Bernoulli(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.BERNOULLI, *e) {

    override fun evaluate(): Expr {

        val result: Expr
        var N = get(0) as IntegerNum

        // TODO clean up with operator overloading

        if (N.isZero)
            result = IntegerNum.ONE
        else if (N.isOne)
            result = NumberExpr.parse("0.5").negate()
        else {
            var res: NumberExpr = IntegerNum.ZERO
            val n = N.intValue()

            for (i in 0 until n) {
                val bin = Binomial(IntegerNum((n + 1).toLong()))
                bin.add(IntegerNum(i.toLong()))
                val t = bin.eval() as IntegerNum
                //System.out.println(t.toString());

                val bern = Bernoulli(IntegerNum(i.toLong()))
                var b = bern.eval() as NumberExpr
                //System.out.println(b);

                b = t.multiply(b)

                res = res.add(b)
            }

            res = res.negate()
            N = N.add(IntegerNum.ONE)
            res = res.divide(N)

            result = res
        }

        return result
    }

    override fun validate() {
        validateParameterCount(1)
        validateNumberType(0, NumberExpr.INTEGER)
    }
}