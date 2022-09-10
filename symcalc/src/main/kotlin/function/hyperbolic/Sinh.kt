package org.cerion.symcalc.function.hyperbolic

import org.cerion.math.bignum.decimal.sinh
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Exp
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Subtract
import org.cerion.symcalc.function.trig.TrigBase
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec
import kotlin.math.sinh

class Sinh(e: Expr) : HyperbolicBase(e) {

    override fun evaluate(e: Expr): Expr {
        when(e) {
            is Integer -> {
                if (e.isZero)
                    return Integer.ZERO
                else if (e.isNegative)
                    return Minus(Sinh(e.unaryMinus()))
            }
            is Log -> {
                if (e.size == 1) // Must be natural log
                    return (e[0] - (Integer.ONE / e[0])) / Integer.TWO
            }
        }

        // Attempt to evaluate
        val result = Exp(e) - Exp(Minus(e))
        if (result !is Subtract)
            return result / Integer.TWO

        return this
    }

    override fun evaluateAsDouble(d: Double): Double = sinh(d)

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return RealBigDec(
            x.value.sinh(RealBigDec.getStoredPrecision(x.precision)),
            x.precision
        )
    }
}