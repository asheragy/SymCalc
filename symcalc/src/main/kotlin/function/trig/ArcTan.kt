package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.extensions.arctan
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec

class ArcTan(e: Expr) : TrigBase(e) {

    override fun evaluateAsDouble(d: Double): Double = kotlin.math.atan(d)

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                if (e.isOne)
                    return Divide(Pi(), Integer(4))
                else if (e.isZero)
                    return Integer.ZERO
            }
            is Infinity -> return Divide(Pi(), Integer(2))
            is ComplexInfinity -> return Indeterminate()
            is Power -> {
                if (e[0] == Integer(3)) {
                    if (e[1] == Rational(-1, 2))
                        return Divide(Pi(), Integer(6))
                    else if (e[1] == Rational.HALF)
                        return Divide(Pi(), Integer(3))
                }
            }
            is Times -> {
                if (e[0] == Rational.THIRD && e[1] == Power(Integer(3), Rational.HALF))
                    return Divide(Pi(), Integer(6))
            }
        }

        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return RealBigDec(x.value.arctan(x.maxStoredPrecision), x.precision)
    }
}