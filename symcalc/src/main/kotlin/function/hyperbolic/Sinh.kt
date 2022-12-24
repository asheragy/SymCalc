package org.cerion.symcalc.function.hyperbolic

import org.cerion.math.bignum.decimal.sinh
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.*
import org.cerion.symcalc.function.trig.Sin
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec
import org.cerion.symcalc.number.RealDouble
import kotlin.math.sinh

class Sinh(e: Any) : HyperbolicBase(e) {

    override fun evaluate(e: Expr): Expr {
        when(e) {
            is Integer -> {
                if (e.isZero)
                    return Integer.ZERO
                else if (e.isNegative)
                    return Minus(Sinh(e.unaryMinus()))
            }
            is RealDouble -> return RealDouble(sinh(e.value))
            is RealBigDec -> {
                return RealBigDec(
                    e.value.sinh(RealBigDec.getStoredPrecision(e.precision)),
                    e.precision)
            }
            is Infinity -> return Infinity()
            is Log -> {
                if (e.size == 1) // Must be natural log
                    return (e[0] - (Integer.ONE / e[0])) / Integer.TWO
            }
        }

        // Attempt to evaluate
        var result = Exp(e) - Exp(Minus(e))
        if (result !is Plus)
            return result / Integer.TWO

        // -i*sin(ix)
        result = Complex(0, -1) * Sin(I() * e)
        if (result is Times && result.args.any { it is Sin })
            return this

        return result
    }
}