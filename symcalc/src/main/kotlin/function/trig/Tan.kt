package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.decimal.tan
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import kotlin.math.tan

class Tan(e: Any) : TrigBase(e), StandardTrigFunction {

    override fun evaluateAsDouble(d: Double): Double = tan(d)

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        // Pi * Integer = 0
        if (e is Integer)
            return Integer.ZERO

        if (e is Rational) {
            val ratio =
                    when(e.denominator) {
                        Integer((2)) -> ComplexInfinity()
                        Integer(3) -> sqrt3
                        Integer(4) -> Integer.ONE
                        Integer(6) -> oneOverSqrt3
                        else -> return this
                    }

            val mod = e.numerator % (Integer.TWO * e.denominator)
            val position = mod.intValue().toDouble() / (e.denominator.intValue() * 2)
            if (position in 0.250001..0.50 || position in 0.750001..1.0)
                return Minus(ratio).eval()

            return ratio
        }

        return this // Could not evaluate
    }

    override fun evaluate(e: Expr): Expr {
        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        return RealBigDec(
            x.value.tan(RealBigDec.getStoredPrecision(x.precision)),
            x.precision
        )
    }

    companion object {
        val sqrt3 = Power(Integer(3), Rational(1,2))
        val oneOverSqrt3 = Power(Integer(3), Rational(-1,2))
    }
}