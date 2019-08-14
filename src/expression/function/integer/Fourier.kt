package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.number.NumberExpr

class Fourier(vararg e: Expr) : FunctionExpr(Function.FOURIER, *e) {

    override fun evaluate(): Expr {
        return recursiveFFT(get(0).asList())
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }

    private fun recursiveFFT(list: ListExpr): ListExpr {
        val N = list.size
        if (N == 1)
            return list

        var result = ListExpr()
        val a0 = ListExpr()
        val a1 = ListExpr()
        run {
            var i = 0
            while (i < N) {
                a0.add(list[i])
                a1.add(list[i + 1])
                i+= 2
            }
        }

        var w = NumberExpr.parse("1")
        val wN = NumberExpr.parse("1i")

        val y0 = recursiveFFT(a0)
        val y1 = recursiveFFT(a1)

        //println("y0 = $y0")
        //println("y1 = $y1")

        for (k in 0 until y0.size) {
            val num1 = y0[k] as NumberExpr
            val num2 = w * (y1[k] as NumberExpr)
            result.add(num1 + num2)
            result.add(num1 - num2)

            w*= wN
            //w2 = w2.multiply(Number.getNumber("-1"));
        }

        // TODO_LP Check this class again, add tests, etc

        //Fix order
        val temp = ListExpr()
        for (i in 0 until y0.size) {
            temp.add(result[i])
            temp.add(result[i + y0.size])
        }
        result = temp

        return result

    }
}
