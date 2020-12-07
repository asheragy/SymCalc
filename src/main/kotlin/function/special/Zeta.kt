package org.cerion.symcalc.function.special

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.function.FunctionExpr
import org.nevec.rjm.BigDecimalMath
import java.math.MathContext

class Zeta(vararg e: Expr) : FunctionExpr(*e) {
    override fun evaluate(): Expr {

        val e = get(0)
        if (e is RealBigDec) {
            val n = e.value.toInt()
            return RealBigDec(BigDecimalMath.zeta(n, MathContext(e.value.precision())), e.precision)
        }

        return this;
    }


}