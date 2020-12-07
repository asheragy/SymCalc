package org.cerion.symcalc.function.calculus


import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.arithmetic.Subtract
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.trig.Cos
import org.cerion.symcalc.function.trig.Sin
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr

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
                is Sin -> result = Times(D(e[0], x), Cos(e[0]))
                is Cos -> result = Times(Integer(-1), D(e[0], x), Sin(e[0]))

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
