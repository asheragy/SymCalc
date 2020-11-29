package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.constant.Indeterminate
import org.cerion.symcalc.expression.constant.Infinity
import org.cerion.symcalc.expression.function.arithmetic.Exp
import org.cerion.symcalc.expression.function.arithmetic.Log
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import kotlin.math.tanh

class Tanh(e: Expr) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double = tanh(d)

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                if (e.isZero)
                    return Integer.ZERO
            }
            is Infinity -> return Integer.ONE
            is ComplexInfinity -> return Indeterminate()
            is Log -> {
                if (e.size == 1) {
                    val x = e[0]
                    return (x * x - Integer.ONE) / (x * x + Integer.ONE)
                }
            }
        }

        // Attempt to evaluate
        val e2x = Exp(Integer.TWO * e).eval()
        if (e2x !is Power)
            return (e2x - Integer.ONE) / (e2x + Integer.ONE)

        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        if (x.isZero)
            return RealBigDec.ZERO
        else if (x.isNegative)
            return Minus(Tanh(x.unaryMinus())).eval()

        val exp = Exp(x * Integer(2)).eval()
        return (exp - Integer.ONE) / (exp + Integer.ONE)
    }
}