package org.cerion.symcalc.function.hyperbolic

import org.cerion.math.bignum.decimal.tanh
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Exp
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble
import kotlin.math.tanh

class Tanh(e: Expr) : HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                if (e.isZero)
                    return Integer.ZERO
            }
            is RealDouble -> return RealDouble(tanh(e.value))
            is RealBigDec -> return RealBigDec(e.value.tanh(RealBigDec.getStoredPrecision(e.precision)), e.precision)
            is Infinity -> return Integer.ONE
            is ComplexInfinity -> return Indeterminate()
            is Log -> {
                if (e.size == 1) {
                    val x = e[0]
                    return (x * x - Integer.ONE) / (x * x + Integer.ONE)
                }
            }
        }

        // Attempt to evaluate
        val e2x = Exp(Integer.TWO * e).eval()
        if (e2x !is Power)
            return (e2x - Integer.ONE) / (e2x + Integer.ONE)

        return this
    }
}