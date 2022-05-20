package org.cerion.symcalc.function.trig

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
import kotlin.math.tanh

class Tanh(e: Expr) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double = tanh(d)

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                if (e.isZero)
                    return Integer.ZERO
            }
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

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return RealBigDec(
            x.value.tanh(RealBigDec.getStoredPrecision(x.precision)),
            x.precision
        )
    }
}