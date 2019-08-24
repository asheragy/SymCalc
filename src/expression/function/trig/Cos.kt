package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.nevec.rjm.BigDecimalMath
import kotlin.math.cos

class Cos(vararg e: Expr) : TrigBase(Function.COS, *e), StandardTrigFunction {

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

    override fun evaluateAsBigDecimal(n: RealBigDec): RealBigDec {
        return RealBigDec(BigDecimalMath.cos(n.value))
    }

    override fun evaluate(e: Expr): Expr {
        return this
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }
}
