package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealBigDec
import org.nevec.rjm.BigDecimalMath

class ArcCos(e: Any) : TrigBase(e) {
    override fun evaluateAsDouble(d: Double): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluate(e: Expr): Expr {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        return RealBigDec(BigDecimalMath.acos(x.value))
    }
}