package org.cerion.symcalc.function.trig

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble

sealed interface StandardTrigFunction {
    fun evaluatePiFactoredOut(e: Expr): Expr
}

abstract class TrigBase protected constructor(vararg e: Any) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.LISTABLE.value

    protected abstract fun evaluateAsDouble(d: Double): Double
    protected abstract fun evaluate(e: Expr): Expr
    protected abstract fun evaluateAsBigDecimal(x: RealBigDec): Expr

    public override fun evaluate(): Expr {
        val e = get(0)

        if (args.size == 2) {
            val y = get(1)
            if (this is ArcTan && e is NumberExpr && y is NumberExpr)
                return evaluate(e, y)
        }

        if (e is RealDouble)
            return RealDouble(evaluateAsDouble(e.value))
        if (e is RealBigDec)
            return evaluateAsBigDecimal(e)

        if (this is StandardTrigFunction) {
            if (e is ComplexInfinity)
                return Indeterminate()

            if (e is NumberExpr && e.isZero)
                return evaluatePiFactoredOut(Integer.ZERO)

            if (e is Pi)
                return evaluatePiFactoredOut(Integer.ONE)

            if (e is Times && e.args.contains(Pi())) {
                val args = e.args.toMutableList()
                args.remove(Pi())
                return evaluatePiFactoredOut(Times(*args.toTypedArray()).eval())
            }
        }

        return evaluate(e)
    }
}
