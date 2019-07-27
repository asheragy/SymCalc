package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr

abstract class TrigBase protected constructor(t: Function, vararg e: Expr) : FunctionExpr(t, *e) {

    override val properties: Int
        get() = Expr.Properties.LISTABLE.value

    protected abstract fun evaluate(num: NumberExpr): Expr
    protected abstract fun evaluate(e: Expr): Expr

    public override fun evaluate(): Expr {
        val n = get(0)
        if (n.isNumber) {
            if (n.isNumericalEval) {
                n as NumberExpr
                return evaluate(n)
            }
        }

        return evaluate(n)
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
