package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.extensions.arccosh
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.number.RealBigDec

class ArcCosh(e: Expr) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double {
        TODO("Not yet implemented")
    }

    override fun evaluate(e: Expr): Expr {
        TODO("Not yet implemented")
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        if (x < RealBigDec("1.0")) {
            TODO("Add complex result")
        }

        return RealBigDec(x.value.arccosh(x.maxStoredPrecision), x.precision)
    }

}