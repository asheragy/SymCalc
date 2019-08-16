package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.integer.Mod
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.nevec.rjm.BigDecimalMath
import kotlin.math.tan

class Tan(vararg e: Expr) : TrigBase(Function.TAN, *e), StandardTrigFunction {

    override fun evaluateAsBigDecimal(n: RealBigDec): RealBigDec = RealBigDec(BigDecimalMath.tan(n.value))
    override fun evaluateAsDouble(d: Double): Double = tan(d)

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        // Pi * Integer = 0
        if (e is Integer)
            return Integer.ZERO

        if (e is Rational) {
            val mod = Mod(e.numerator, Times(Integer.TWO, e.denominator).eval().asInteger()).eval().asInteger().intValue()

            val ratio =
                    when(e.denominator) {
                        Integer((2)) -> ComplexInfinity()
                        Integer(3) -> sqrt3
                        Integer(4) -> Integer.ONE
                        Integer(6) -> oneOverSqrt3
                        else -> return this
                    }

            val position = mod.toDouble() / (e.denominator.intValue() * 2)
            if (position in 0.250001..0.50 || position in 0.750001..1.0)
                return Minus(ratio).eval()

            return ratio
        }

        return this // Could not evaluate
    }

    override fun evaluate(e: Expr): Expr {
        return this
    }

    companion object {
        val sqrt3 = Power(Integer(3), Rational(1,2))
        val oneOverSqrt3 = Power(Integer(3), Rational(-1,2))
    }
}