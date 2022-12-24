package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.E
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.RealBigDec

class Exp(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val e = get(0)

        if (e is ComplexInfinity)
            return Indeterminate()
        if (e is RealBigDec)
            return e.exp()

        return Power(E(), e).eval()
    }

    override fun validate() {
        validateParameterCount(1)
    }

    override val properties: Int
        get() = Properties.LISTABLE.value
}