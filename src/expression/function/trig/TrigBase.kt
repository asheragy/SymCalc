package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealNum_Double

abstract class TrigBase protected constructor(t: Function, vararg e: Expr) : FunctionExpr(t, *e) {

    override val properties: Int
        get() = Expr.Properties.LISTABLE.value

    protected abstract fun evaluateAsDouble(d: Double): Expr
    protected abstract fun evaluate(e: Expr): Expr
    protected abstract fun evaluatePiFactoredOut(e: Expr): Expr

    public override fun evaluate(): Expr {
        val e = get(0)
        if (e.isNumber) {
            e as NumberExpr
            if (e is RealNum_Double) {
                return evaluateAsDouble(e.toDouble())
            }
        }

        if (e.isNumber && e.asNumber().isZero)
            return evaluatePiFactoredOut(IntegerNum.ZERO)

        if (e is Pi)
            return evaluatePiFactoredOut(IntegerNum.ONE)

        if (e is Times) {
            val index = e.args.indexOfFirst { it is Pi }
            val times = Times()
            for (i in 0 until e.args.size)
                if (i != index)
                    times.add(e.args[i])

            return evaluatePiFactoredOut(times.eval())
        }

        if (e is Divide && e.args[0] is Pi) {
            return evaluatePiFactoredOut(Divide(IntegerNum.ONE, e.args[1]).eval())
        }

        return evaluate(e)
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
