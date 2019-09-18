package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble

interface StandardTrigFunction {
    fun evaluatePiFactoredOut(e: Expr): Expr
}

abstract class TrigBase protected constructor(vararg e: Expr) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.LISTABLE.value

    protected abstract fun evaluateAsDouble(d: Double): Double
    protected abstract fun evaluate(e: Expr): Expr
    protected abstract fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec

    public override fun evaluate(): Expr {
        val e = get(0)

        if (e is RealDouble)
            return RealDouble(evaluateAsDouble(e.value))
        if (e is RealBigDec)
            return evaluateAsBigDecimal(e)

        if (this is StandardTrigFunction) {
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

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
