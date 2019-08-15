package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.integer.Mod
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import kotlin.math.tan

class Tan(vararg e: Expr) : TrigBase(Function.TAN, *e), StandardTrigFunction {
    override fun evaluateAsDouble(d: Double): Double = tan(d)

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        // Pi * Integer = 0
        if (e is IntegerNum)
            return IntegerNum.ZERO

        if (e is Rational) {
            val mod = Mod(e.numerator, Times(IntegerNum.TWO, e.denominator).eval().asInteger()).eval().asInteger().intValue()

            val ratio =
                    when(e.denominator) {
                        IntegerNum((2)) -> ComplexInfinity()
                        IntegerNum(3) -> sqrt3
                        IntegerNum(4) -> IntegerNum.ONE
                        IntegerNum(6) -> oneOverSqrt3
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
        val sqrt3 = Power(IntegerNum(3), Rational(1,2))
        val oneOverSqrt3 = Power(IntegerNum(3), Rational(-1,2))
    }
}