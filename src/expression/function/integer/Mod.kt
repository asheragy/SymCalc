package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.*
import kotlin.math.floor
import kotlin.math.min

class Mod(vararg e: Expr) : FunctionExpr(Function.MOD, *e) {

    // FEAT can work with non-integers

    override fun evaluate(): Expr {
        var a = get(0) as NumberExpr
        var b = get(1) as NumberExpr

        // TODO need better way of doing this
        if (a.precision != b.precision) {
            val min = min(a.precision, b.precision)
            a = a.evaluate(min)
            b = b.evaluate(min)
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
            return RealBigDec(c)
        }

        return this
    }

    override fun validate() {
        validateParameterCount(2)
        validateParameterType(0, ExprType.NUMBER)
        validateParameterType(1, ExprType.NUMBER)
    }
}
