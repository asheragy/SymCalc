package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.NumberExpr

class Fourier(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.FOURIER, *e) {

    override fun evaluate(): Expr {
        return Recursive_FFT(get(0).asList())
    }

    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }

    fun Recursive_FFT(list: ListExpr): ListExpr {
        val N = list.size()
        if (N == 1)
            return list

        println("FFT: " + list.toString())
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

        val y0 = Recursive_FFT(a0)
        val y1 = Recursive_FFT(a1)
        //MathList y0 = new MathList();
        //MathList y1 = new MathList();
        //y0.add(Number.getNumber("4"));
        //y0.add(Number.getNumber("-2"));
        //y1.add(Number.getNumber("2"));
        //y1.add(Number.getNumber("-2"));

        println("y0 = " + y0.toString())
        println("y1 = " + y1.toString())

        for (k in 0 until y0.size()) {
            val num1 = y0[k] as NumberExpr
            val num2 = w.multiply(y1[k] as NumberExpr)
            result.add(num1.add(num2))
            result.add(num1.subtract(num2))

            w = w.multiply(wN)
            //w2 = w2.multiply(Number.getNumber("-1"));
        }


        //Fix order
        val temp = ListExpr()
        for (i in 0 until y0.size()) {
            temp.add(result[i])
            temp.add(result[i + y0.size()])
        }
        result = temp

        return result

    }
}
