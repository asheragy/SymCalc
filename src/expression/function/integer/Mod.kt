package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.number.*
import kotlin.math.min

class Mod(vararg e: Expr) : FunctionExpr(*e) {

    // FEAT can work with non-integers

    override fun evaluate(): Expr {
        var a = get(0) as NumberExpr
        var b = get(1)

        // TODO_LP need better way of doing this
        if (b is NumberExpr && a.precision != b.precision) {
            val min = min(a.precision, b.precision)
            a = a.toPrecision(min)
            b = b.toPrecision(min)
        }

        if (a is Integer && b is Integer)
            return a.rem(b)

        if (a is RealDouble && b is RealDouble) {
            var c = a.value.rem(b.value)
            if (c < 0)
                c+= b.value

            return RealDouble(c)
        }

        if (a is RealBigDec && b is RealBigDec) {
            var c = a.value.rem(b.value)
            if (c.signum() < 0)
                c+= b.value
            return RealBigDec(c, a.precision)
        }

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
