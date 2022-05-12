package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.extensions.arccos
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.RealBigDec
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
        return RealBigDec(x.value.arccos(x.maxStoredPrecision), x.precision)
    }
}