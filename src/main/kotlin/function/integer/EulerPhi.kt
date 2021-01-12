package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.nevec.rjm.BigIntegerMath

class EulerPhi(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {

        val n = get(0)
        if (n is Integer) {
            return Integer(BigIntegerMath.eulerPhi(n.intValue()))
        }

        return this
    }
}