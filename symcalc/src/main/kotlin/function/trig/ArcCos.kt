package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.decimal.arccos
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Sqrt
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import kotlin.math.acos

class ArcCos(e: Any) : TrigBase(e) {

    // TODO Complex and more trig tables

    override fun evaluateAsDouble(d: Double): Double = acos(d)

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is ComplexInfinity -> return ComplexInfinity()
            is Integer -> {
                when (e) {
                    Integer.ONE -> return Integer.ZERO
                    Integer.NEGATIVE_ONE -> return Pi()
                    Integer.ZERO -> return Divide(Pi(), 2).eval()
                }
            }
            is Rational -> {
                if (e == Rational(1,2))
                    return Pi() / 3
                if (e == Rational(-1,2))
                    return Pi() * Rational(2,3)
            }
            is Complex -> {
                val oneMinusZ = Integer.ONE - e.square()
                return Rational.HALF * Pi() + I() * Log(I() * e + Sqrt(oneMinusZ))
            }
        }

        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return RealBigDec(x.value.arccos(x.maxStoredPrecision), x.precision)
    }
}