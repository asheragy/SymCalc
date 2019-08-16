package org.cerion.symcalc.expression.function.calculus


import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.VarExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.function.arithmetic.Subtract
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.trig.Cos
import org.cerion.symcalc.expression.function.trig.Sin
import org.cerion.symcalc.expression.number.Integer

class D(vararg e: Expr) : FunctionExpr(Function.D, *e) {

    override fun evaluate(): Expr {
        val e = get(0)
        val x = get(1) as VarExpr

        if (e.isNumber || e.isConst)
            return Integer.ZERO

        if (e.isVariable) {
            return if (e.asVar().equals(x))
                Integer.ONE
            else
                Integer.ZERO
        }

        if (e.isFunction) {
            val func = e as FunctionExpr
            val result: FunctionExpr

            when (func.value) {
                Function.PLUS -> result = Plus(*func.args.map { D(it, x) }.toTypedArray())
                Function.SUBTRACT -> result = Subtract(*func.args.map { D(it, x) }.toTypedArray())
                Function.SIN -> result = Times(D(func[0], x), Cos(func[0]))
                Function.COS -> result = Times(Integer(-1), D(func[0], x), Sin(func[0]))

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
