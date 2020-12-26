package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.constant.E
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.RealBigDec
import org.nevec.rjm.BigDecimalMath

class Exp(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = get(0)
        if (e is RealBigDec) {
            val t = e.forcePrecision(RealBigDec.getStoredPrecision(e.precision))
            return RealBigDec(BigDecimalMath.exp(t), e.precision)
        }

        return Power(E(), get(0)).eval()
    }

    override fun validate() {
        validateParameterCount(1)
    }

    override val properties: Int
        get() = Properties.LISTABLE.value
}