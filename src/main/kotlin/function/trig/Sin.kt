package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.extensions.sin
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import kotlin.math.sin

class Sin(e: Any) : TrigBase(e), StandardTrigFunction {

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }

    override fun evaluateAsDouble(d: Double): Double = sin(d)

    override fun evaluate(e: Expr): Expr {
        return this
    }

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        // Pi * Integer = 0
        if (e is Integer)
            return Integer.ZERO

        if (e is Rational) {
            val ratio =
            when(e.denominator) {
                Integer((2)) -> Integer.ONE
                Integer(3) -> sqrt3Over2
                Integer(4) -> oneOverSqrt2
                Integer(6) -> Rational(1,2)
                else -> return this
            }

            val mod = e.numerator % (Integer.TWO * e.denominator)
            if (mod > e.denominator)
                return Minus(ratio).eval()

            return ratio
        }

        return this // Could not evaluate
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        return RealBigDec(
            x.value.sin(RealBigDec.getStoredPrecision(x.precision)),
            x.precision
        )
    }
}
