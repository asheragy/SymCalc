package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.extensions.cos
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import kotlin.math.cos

class Cos(e: Any) : TrigBase(e), StandardTrigFunction {

    override fun evaluateAsDouble(d: Double): Double = cos(d)

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        if (e is Integer) {
            if (e.isOdd)
                return Integer.NEGATIVE_ONE
            return Integer.ONE
        }

        if (e is Rational) {
            var offset = Integer.ZERO

            val ratio =
            when(e.denominator) {
                Integer((2)) -> Integer.ZERO
                Integer(3) -> {
                    offset = Integer.ONE
                    Rational.HALF
                }
                Integer(4) -> {
                    offset = Integer.TWO
                    oneOverSqrt2
                }
                Integer(6) -> {
                    offset = Integer(3)
                    sqrt3Over2
                }
                else -> return this
            }

            val mod = (e.numerator + offset) % (Integer.TWO * e.denominator)
            if (mod >= e.denominator)
                return Minus(ratio).eval()

            return ratio
        }

        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        return RealBigDec(
            x.value.cos(RealBigDec.getStoredPrecision(x.precision)),
            x.precision
        )
    }

    override fun evaluate(e: Expr): Expr {
        return this
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }
}
