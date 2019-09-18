package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.Rational

class Divide(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        // Transform x / y^z = x * y^-z
        if (b is Power && b[1] is NumberExpr) {
            return Times(a, Power(b[0], b[1].asNumber().unaryMinus())).eval()
        }

        if (b is NumberExpr) {
            if (b.isZero)
                return ComplexInfinity()

            if (b.isOne) //Identity
                return a

            // Factor out rational number if it can't be evaluated
            if (b is Integer && a !is NumberExpr)
                return Times(Rational(Integer.ONE, b), a).eval()
        }

        if (a is NumberExpr && b is NumberExpr)
            return a / b

        if (b is ComplexInfinity)
            return Integer.ZERO

        return this
    }

    override fun toString(): String = if (size == 2) get(0).toString() + " / " + get(1) else super.toString()

    override fun validate() {
        validateParameterCount(2)
    }
}
