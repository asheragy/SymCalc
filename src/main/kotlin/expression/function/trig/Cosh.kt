package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealBigDec
import org.nevec.rjm.BigDecimalMath
import kotlin.math.cosh

class Cosh(e: Expr) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double = cosh(d)

    override fun evaluate(e: Expr): Expr {
        TODO("Not yet implemented")
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        val t = x.forcePrecision(RealBigDec.getStoredPrecision(x.precision))
        return RealBigDec(BigDecimalMath.cosh(t), x.precision)
    }
}