package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.decimal.cosh
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Exp
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec
import kotlin.math.cosh

class Cosh(e: Expr) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double = cosh(d)

    override fun evaluate(e: Expr): Expr {

        when (e) {
            is Integer -> {
                if (e.isZero)
                    return Integer.ONE
            }
            is Infinity -> return Infinity()
            is ComplexInfinity -> return Indeterminate()
            is Log -> {
                if (e.size == 1) {
                    return ((Integer.ONE / e[0]) + e[0]) / Integer.TWO
                }
            }
        }

        // Attempt to evaluate
        val result = Exp(e) + Exp(Minus(e))
        if (result !is Plus)
            return result / Integer.TWO

        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return RealBigDec(
            x.value.cosh(RealBigDec.getStoredPrecision(x.precision)),
            x.precision
        )
    }
}