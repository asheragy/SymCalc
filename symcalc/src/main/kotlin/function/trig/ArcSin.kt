package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.decimal.arcsin
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Sqrt
import org.cerion.symcalc.function.arithmetic.Subtract
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import kotlin.math.asin

class ArcSin(e: Any) : TrigBase(e) {

    // TODO Complex and more trig tables

    override fun evaluateAsDouble(d: Double): Double = asin(d)

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is ComplexInfinity -> return ComplexInfinity()
            is Integer -> {
                when (e) {
                    Integer.ONE -> return Divide(Pi(), 2).eval()
                    Integer.NEGATIVE_ONE -> return Divide(Pi(), -2).eval()
                    Integer.ZERO -> return Integer.ZERO
                }
            }
            is Complex -> {
                return Integer(-1) * I() * Log(I() * e + Sqrt(Subtract(1, e.square())))
            }
            is Rational -> {
                if(e == Rational.HALF)
                    return Pi() / 6
                if(e == Rational.HALF.unaryMinus())
                    return Rational(-1,6) * Pi()
            }
        }

        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return RealBigDec(
            x.value.arcsin(RealBigDec.getStoredPrecision(x.precision)),
            x.precision
        )
    }
}