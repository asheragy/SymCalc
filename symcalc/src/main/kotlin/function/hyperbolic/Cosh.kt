package org.cerion.symcalc.function.hyperbolic

import org.cerion.math.bignum.decimal.cosh
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Exp
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.function.trig.Cos
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble
import kotlin.math.cosh

class Cosh(e: Any) : HyperbolicBase(e) {

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                if (e.isZero)
                    return Integer.ONE
            }
            is RealDouble -> return RealDouble(cosh(e.value))
            is RealBigDec -> return RealBigDec(
                e.value.cosh(RealBigDec.getStoredPrecision(e.precision)),
                e.precision
            )
            is Infinity -> return Infinity()
            is ComplexInfinity -> return Indeterminate()
            is Log -> {
                if (e.size == 1) {
                    return ((Integer.ONE / e[0]) + e[0]) / Integer.TWO
                }
            }
        }

        // Attempt to evaluate
        var result = Exp(e) + Exp(Minus(e))
        if (result !is Plus)
            return result / Integer.TWO

        // Cosh(x) = Cos(ix)
        result = Cos(I() * e).eval()
        if (result !is Cos)
            return result

        return this
    }
}