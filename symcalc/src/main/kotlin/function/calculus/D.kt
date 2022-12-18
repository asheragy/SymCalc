package org.cerion.symcalc.function.calculus


import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.*
import org.cerion.symcalc.function.trig.*
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr

class D(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = get(0)
        val x = get(1) as VarExpr

        if (e is NumberExpr || e is ConstExpr)
            return Integer.ZERO

        if (e is VarExpr) {
            return if (e == x)
                Integer.ONE
            else
                Integer.ZERO
        }

        if (e is FunctionExpr) {
            val result: FunctionExpr

            when (e) {
                is Plus -> result = Plus(*e.args.map { D(it, x) }.toTypedArray())
                is Subtract -> result = Subtract(*e.args.map { D(it, x) }.toTypedArray())
                is Times -> {
                    val u = e[0]
                    val v = e[1]
                    if (e.args.size > 2)
                        TODO("Not Implemented")

                    result = Plus(Times(D(u, x), v), Times(D(v, x), u))
                }
                is Power -> {
                    val a = e[0]
                    val b = e[1]

                    // x^b
                    if (a.equals(x))
                        result = Times(b, Power(a, Subtract(b, Integer.ONE)))
                    // a^x
                    else
                        result = Times(e, Log(a), D(b, x))

                }
                is Csc,
                is Sec,
                is StandardTrigFunction -> {
                    val arg = e[0]
                    val argDx = D(arg, x)
                    result = when(e) {
                        is Sin -> Times(argDx, Cos(arg))
                        is Cos -> Times(Integer(-1), argDx, Sin(arg))
                        is Tan -> Times(argDx, Power(Sec(arg), 2))
                        is Sec -> Times(argDx, Sec(arg), Tan(arg))
                        is Csc -> Times(Minus(argDx), Cot(arg), Csc(arg))
                        is Cot -> Times(Minus(argDx), Power(Csc(arg), 2))
                        else -> TODO("This should not even be required")
                    }
                }

                else -> return this
            }

            return result.eval()
        }

        return this
    }

    override fun validate() {
        validateParameterCount(2)
        validateParameterType(1, ExprType.VARIABLE)
    }
}
