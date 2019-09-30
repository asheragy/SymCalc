package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr

class Divide(x: Any, y: Any) : FunctionExpr(x, y) {

    override fun evaluate(): Expr {
        val x = get(0)
        val y = get(1)

        if (y is NumberExpr) {
            if (y.isZero)
                return ComplexInfinity()

            if (y.isOne) //Identity
                return x

            if (x is NumberExpr)
                return x / y
        }

        if (y is ComplexInfinity)
            return Integer.ZERO

        // Reduce remaining division to multiplication
        return x * Power(y, -1)
    }

    override fun toString(): String = if (size == 2) get(0).toString() + " / " + get(1) else super.toString()
}
