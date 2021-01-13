package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.nevec.rjm.Euler

class EulerE(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {

        val n = get(0)
        if (n is Integer) {
            if (n.isOdd)
                return Integer.ZERO

            val e = Euler()
            val t = (n / Integer(2)) as Integer

            val eval = e.at(t.intValue())
            if (t.isOdd)
                return Integer(eval).unaryMinus()

            return Integer(eval)
        }

        return this
    }
}