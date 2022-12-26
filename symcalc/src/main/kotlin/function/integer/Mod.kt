package org.cerion.symcalc.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealDouble

class Mod(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val a = get(0) as NumberExpr
        val b = get(1)

        if (b is NumberExpr)
            return a % b

        if (a is Integer || a is Rational) {
            // Numerical eval to get closest integer value as multiplier for unknown value B
            val bn = b.eval(MachinePrecision)
            val whole = Divide(a, bn).eval()
            if (whole is RealDouble)
                return a + (whole.floor().unaryMinus() * b)
        }

        return this
    }

    override fun validate() {
        validateParameterCount(2)
        validateParameterType(0, ExprType.NUMBER)
    }
}
