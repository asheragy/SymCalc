package org.cerion.symcalc.function.special

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.number.RealDouble
import org.cerion.symcalc.function.FunctionExpr
import org.nevec.rjm.BigDecimalMath

class PolyGamma(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = get(0)
        if (e is RealDouble) {
            return RealDouble(BigDecimalMath.psi(e.value))
        }

        return this
    }


}