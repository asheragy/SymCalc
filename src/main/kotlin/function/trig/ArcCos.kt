package org.cerion.symcalc.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Sqrt
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import kotlin.math.acos

class ArcCos(e: Any) : TrigBase(e) {

    // FEAT Complex and more trig tables

    override fun evaluateAsDouble(d: Double): Double = acos(d)

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                when (e) {
                    Integer.ONE -> return Integer.ZERO
                    Integer.NEGATIVE_ONE -> return Pi()
                    Integer.ZERO -> return Divide(Pi(), 2).eval()
                }
            }
        }

        return this
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        if (x > RealDouble(0.7)) // bypasses the need for calculating Pi/2
            return ArcSin(Sqrt(Integer(1) - x.square())).eval()

        return Divide(Pi(), 2) - ArcSin(x)
    }
}