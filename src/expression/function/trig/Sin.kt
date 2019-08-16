package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.integer.Mod
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.nevec.rjm.BigDecimalMath
import kotlin.math.sin

class Sin(vararg e: Expr) : TrigBase(Function.SIN, *e), StandardTrigFunction {

    override fun evaluateAsBigDecimal(n: RealBigDec): RealBigDec = RealBigDec(BigDecimalMath.sin(n.value))
    override fun evaluateAsDouble(d: Double): Double = sin(d)

    override fun evaluate(e: Expr): Expr {
        return this
    }

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        // Pi * Integer = 0
        if (e is Integer)
            return Integer.ZERO

        if (e is Rational) {
            val mod = Mod(e.numerator, Times(Integer.TWO, e.denominator).eval().asInteger()).eval().asInteger().intValue()

            val ratio =
            when(e.denominator) {
                Integer((2)) -> Integer.ONE
                Integer(3) -> sqrt3Over2
                Integer(4) -> oneOverSqrt2
                Integer(6) -> Rational(1,2)
                else -> return this
            }

            if (mod > e.denominator.intValue())
                return Minus(ratio).eval()

            return ratio
        }

        return this // Could not evaluate
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }
}
