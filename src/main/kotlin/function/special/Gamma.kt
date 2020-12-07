package org.cerion.symcalc.function.special

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.function.FunctionExpr
import org.nevec.rjm.BigDecimalMath

class Gamma(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {

        val e = get(0)
        if (e is RealBigDec) {
            val t = e.forcePrecision(RealBigDec.getStoredPrecision(e.precision))
            return RealBigDec(BigDecimalMath.Gamma(t), e.precision)
        }

        return this
    }
}