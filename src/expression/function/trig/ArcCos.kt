package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.number.RealBigDec

class ArcCos(e: Any) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluate(e: Expr): Expr {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return Divide(Pi(), 2) - ArcSin(x)
    }
}