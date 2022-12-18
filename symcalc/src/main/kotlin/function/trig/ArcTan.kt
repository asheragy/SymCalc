package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.decimal.arctan
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec

class ArcTan(vararg e: Any) : TrigBase(*e) {

    override fun evaluateAsDouble(d: Double): Double = kotlin.math.atan(d)

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                if (e.isOne)
                    return Pi() / 4
                else if (e == Integer.NEGATIVE_ONE)
                    return Pi() / -4
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

    fun evaluate(x: NumberExpr, y: NumberExpr): Expr {
        if (x.isZero) {
            if (y.isZero)
                return Indeterminate()

            val piOver2 = Pi() / 2
            return if (y.isNegative)
                Minus(piOver2).eval()
            else
                piOver2
        }

        val result = ArcTan(y / x)
        if (x.isNegative) {
            if (y.isNegative)
                return result - Pi()
            else
                return result + Pi()
        }
        else
            return result.eval()
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return RealBigDec(x.value.arctan(x.maxStoredPrecision), x.precision)
    }
}