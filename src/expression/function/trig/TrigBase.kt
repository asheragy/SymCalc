package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealNum_Double
import org.cerion.symcalc.expression.Expr as Expr1

interface StandardTrigFunction {
    fun evaluatePiFactoredOut(e: Expr1): Expr1
}

abstract class TrigBase protected constructor(t: Function, vararg e: Expr1) : FunctionExpr(t, *e) {

    override val properties: Int
        get() = Expr1.Properties.LISTABLE.value

    protected abstract fun evaluateAsDouble(d: Double): Double
    protected abstract fun evaluate(e: Expr1): Expr1

    public override fun evaluate(): Expr1 {
        val e = get(0)
        if (e.isNumber) {
            e as NumberExpr
            if (e is RealNum_Double) {
                return RealNum_Double(evaluateAsDouble(e.value))
            }
        }

        if (this is StandardTrigFunction) {
            if (e.isNumber && e.asNumber().isZero)
                return evaluatePiFactoredOut(IntegerNum.ZERO)

            if (e is Pi)
                return evaluatePiFactoredOut(IntegerNum.ONE)

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
