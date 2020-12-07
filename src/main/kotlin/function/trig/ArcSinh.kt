package org.cerion.symcalc.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Sqrt
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.plus

class ArcSinh(e: Expr) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double {
        TODO("Not yet implemented")
    }

    override fun evaluate(e: Expr): Expr {
        TODO("Not yet implemented")
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        if (x.isZero)
            return RealBigDec.ZERO

        return Log(x + Sqrt(1 + x.square())).eval()
    }

}