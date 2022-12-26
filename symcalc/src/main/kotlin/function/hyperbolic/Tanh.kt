package org.cerion.symcalc.function.hyperbolic

import org.cerion.math.bignum.decimal.tanh
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Exp
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.trig.Tan
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble
import kotlin.math.tanh

class Tanh(e: Any) : HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                if (e.isZero)
                    return Integer.ZERO
            }
            is RealDouble -> return RealDouble(tanh(e.value))
            is RealBigDec -> return RealBigDec(e.value.tanh(RealBigDec.getStoredPrecision(e.precision)), e.precision)
            is Infinity -> return Integer(e.direction)
            is ComplexInfinity -> return Indeterminate()
            is Log -> {
                if (e.size == 1) {
                    val x = e[0]
                    return (x * x - Integer.ONE) / (x * x + Integer.ONE)
                }
            }
        }

        // Attempt to evaluate
        var result = Exp(Integer.TWO * e).eval()
        if (result !is Power)
            return (result - Integer.ONE) / (result + Integer.ONE)

        // Tanh(x) = -i*Tan(ix)
        result = Complex(0, -1) * Tan(I() * e)
        if (result is Times && result.args.any { it is Tan })
            return this

        return result
    }
}