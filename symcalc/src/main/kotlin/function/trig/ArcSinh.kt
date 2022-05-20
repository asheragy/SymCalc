package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.decimal.arcsinh
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.number.RealBigDec

class ArcSinh(e: Expr) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double {
        TODO("Not yet implemented")
    }

    override fun evaluate(e: Expr): Expr {
        TODO("Not yet implemented")
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return RealBigDec(x.value.arcsinh(x.maxStoredPrecision), x.precision)
    }

}