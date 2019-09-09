package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.integer.Factorial
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.sin

class Sin(vararg e: Expr) : TrigBase(Function.SIN, *e), StandardTrigFunction {

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
        val mc = MathContext(x.precision+5, RoundingMode.HALF_UP)

        // TODO add 2 pi so input is more normalized
        // TODO optimize then copy to Cos
        var result = x.value
        for(i in 1..100) {
            val n = (i * 2) + 1
            val pow = x.value.pow(n, mc)
            val fact = Factorial(Integer(n)).eval().asInteger()
            val e = pow.divide(BigDecimal(fact.value), mc)

            val t = if (i % 2 == 0)
                        result.add(e, mc)
                    else
                        result.subtract(e, mc)

            if (t == result)
                break

            result = t
        }

        return RealBigDec(result.round(MathContext(x.precision, RoundingMode.HALF_UP)))
    }
}
