package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import kotlin.math.sinh

class Sinh(e: Expr) : TrigBase(e) {

    override fun evaluate(e: Expr): Expr {
        when(e) {
            is Integer -> {
                if (e.isZero)
                    return Integer.ZERO
                else if (e.isNegative)
                    return Minus(Sinh(e.unaryMinus()))
            }
            is Log -> {
                if (e.size == 1) // Must be natural log
                    return (e[0] - (Integer.ONE / e[0])) / Integer.TWO
            }
        }

        // Attempt to evaluate
        val result = Exp(e) - Exp(Minus(e))
        if (result !is Subtract)
            return result / Integer.TWO

        return this
    }

    override fun evaluateAsDouble(d: Double): Double = sinh(d)

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        if (x.isNegative)
            return Minus(Sinh(x.unaryMinus())).eval()
        else if (x.isZero)
            return RealBigDec.ZERO

        if (x.toDouble() > 2.4) {
            // Reduce using Sinh(2x)= 2*Sinh(x)*Cosh(x)
            val xhalf = x / Integer.TWO
            return Times(Integer.TWO, Sinh(xhalf), Cosh(xhalf)).eval()
        }

        // This works too but not as efficient
        //return (Exp(x) - Exp(x.unaryMinus())) / Integer.TWO

        // Taylor series x + x^3/3! + x^5 / 7! + ...
        val xsquared = x * x
        var factorial = Integer.ONE
        var xpow = x
        var result = x

        for(i in 1 until 100) {
            factorial *= Integer(i * 2)
            factorial *= Integer(i * 2 + 1)
            xpow *= xsquared
            val term = xpow / factorial

            val t = (result + term) as RealBigDec
            if (t == result) // TODO in all other functions return t since its 1 iteration more accurate
                return t

            result = t
        }

        throw IterationLimitExceeded()
    }

}