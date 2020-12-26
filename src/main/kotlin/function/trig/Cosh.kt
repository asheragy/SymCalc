package org.cerion.symcalc.function.trig

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.function.arithmetic.Exp
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Plus
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.RealBigDec
import kotlin.math.cosh

class Cosh(e: Expr) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double = cosh(d)

    override fun evaluate(e: Expr): Expr {

        when (e) {
            is Integer -> {
                if (e.isZero)
                    return Integer.ONE
            }
            is Infinity -> return Infinity()
            is ComplexInfinity -> return Indeterminate()
            is Log -> {
                if (e.size == 1) {
                    return ((Integer.ONE / e[0]) + e[0]) / Integer.TWO
                }
            }
        }

        // Attempt to evaluate
        val result = Exp(e) + Exp(Minus(e))
        if (result !is Plus)
            return result / Integer.TWO

        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        if (x.isNegative)
            return Cosh(x.unaryMinus()).eval()
        if (x.isZero)
            return RealBigDec("1.0")

        // Possible formula to use for faster convergence on larger numbers
        // cosh^2(x) = 1+ sinh^2(x)

        // Direct compute but slower
        // return (Exp(x) + Exp(x.unaryMinus())) / Integer.TWO

        val xsquared = x * x
        var term: NumberExpr = Integer.ONE
        var result: NumberExpr = term

        for(n in 1 until 100) {
            term *= xsquared
            term = (term / Integer((2 * n) * (2 * n - 1)))

            val t = result + term
            if (t == result)
                return t

            result = t
        }

        throw IterationLimitExceeded()
    }
}