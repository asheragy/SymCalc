package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble


abstract class HyperbolicBase protected constructor(e: Any) : FunctionExpr(e) {

    override val properties: Int
        get() = Properties.LISTABLE.value

    protected abstract fun evaluateAsDouble(d: Double): Double
    protected abstract fun evaluate(e: Expr): Expr
    protected abstract fun evaluateAsBigDecimal(x: RealBigDec): Expr

    public override fun evaluate(): Expr {
        val e = get(0)

        if (e is RealDouble)
            return RealDouble(evaluateAsDouble(e.value))
        if (e is RealBigDec)
            return evaluateAsBigDecimal(e)

        return evaluate(e)
    }
}
