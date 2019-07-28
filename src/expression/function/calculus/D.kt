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
import org.cerion.symcalc.expression.number.IntegerNum

class D(vararg e: Expr) : FunctionExpr(Function.D, *e) {

    override fun evaluate(): Expr {
        val e = get(0)
        val `var` = get(1) as VarExpr

        if (e.isNumber || e.isConst)
            return IntegerNum.ZERO

        if (e.isVariable) {
            return if (e.asVar().equals(`var`))
                IntegerNum.ONE
            else
                IntegerNum.ZERO
        }

        if (e.isFunction) {
            val func = e as FunctionExpr
            val result: FunctionExpr

            when (func.function) {
                Function.PLUS -> {
                    // TODO use map function here, or similar
                    result = Plus()
                    for (ee in func.args)
                        result.add(D(ee, `var`))
                }

                Function.SUBTRACT -> {
                    result = Subtract()
                    for (ee in func.args)
                        result.add(D(ee, `var`))
                }

                Function.SIN -> result = Times(
                        D(func[0], `var`),
                        Cos(func[0]))

                Function.COS -> result = Times(
                        IntegerNum(-1),
                        D(func[0], `var`),
                        Sin(func[0]))

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
