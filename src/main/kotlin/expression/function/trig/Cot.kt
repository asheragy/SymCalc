package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import kotlin.math.tan

class Cot(e: Any): TrigBase(e), StandardTrigFunction {

    override fun evaluateAsDouble(d: Double): Double = 1 / tan(d)

    override fun evaluate(e: Expr): Expr {
        return this
    }

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        // Pi * Integer = 0
        if (e is Integer)
            return ComplexInfinity()

        if (e is Rational) {
            val ratio =
                    when(e.denominator) {
                        Integer((2)) -> Integer.ZERO
                        Integer(3) -> Tan.oneOverSqrt3
                        Integer(4) -> Integer.ONE
                        Integer(6) -> Tan.sqrt3
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

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        return Divide(Cos(x), Sin(x)).eval() as RealBigDec
    }
}