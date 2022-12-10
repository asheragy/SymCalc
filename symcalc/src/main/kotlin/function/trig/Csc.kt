package org.cerion.symcalc.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.number.RealBigDec

class Csc(e: Any) : TrigBase(e), StandardTrigFunction {
    override fun evaluatePiFactoredOut(e: Expr): Expr {
        TODO("Not yet implemented")
    }

    override fun evaluateAsDouble(d: Double): Double {
        TODO("Not yet implemented")
    }

    override fun evaluate(e: Expr): Expr {
        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        TODO("Not yet implemented")
    }
}