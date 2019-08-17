package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Divide
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

        // Even*Pi = 1 / Odd*Pi = -1
        if (e is Times && e.args.any { it is Pi } && e.args.any { it is Integer }) {
            val removeIntegers = Times()
            e.args.forEach {
                if (it !is Integer)
                    removeIntegers.add(it)
            }
            val cos = Cos(removeIntegers)

            val theInteger = e.args.first { it is Integer } as Integer
            if (theInteger.isEven)
                return Times(Integer.NEGATIVE_ONE, cos).eval()

            return cos.eval()
        }

        // Pi / Integer
        if (e is Divide && e.args[1] == Integer.TWO) {
            return Integer.ZERO
        }

        return this
    }

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(Integer(3), Rational(1,2)))
        val oneOverSqrt2 = Power(Integer.TWO, Rational(-1,2))
    }
}
