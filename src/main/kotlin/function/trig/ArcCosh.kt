package org.cerion.symcalc.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Sqrt
import org.cerion.symcalc.expression.number.RealBigDec

class ArcCosh(e: Expr) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double {
        TODO("Not yet implemented")
    }

    override fun evaluate(e: Expr): Expr {
        TODO("Not yet implemented")
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        if (x.isOne)
            return RealBigDec.ZERO

        if (x < RealBigDec("1.0")) {
            TODO("Add complex result")
        }

        return Log(x + Sqrt(x.square() - 1)).eval()
    }

}