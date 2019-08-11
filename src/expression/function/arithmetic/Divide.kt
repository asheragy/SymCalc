package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.Rational

class Divide(vararg e: Expr) : FunctionExpr(Function.DIVIDE, *e) {

    override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        // Transform x / y^-z = x * y^z
        if (b is Power && b[1] is NumberExpr && b[1].asNumber().isNegative) {
            return Times(a, Power(b[0], b[1].asNumber().unaryMinus())).eval()
        }

        if (b is NumberExpr) {
            if (b.isOne) //Identity
                return a

            // Factor out rational number if it can't be evaluated
            if (b is IntegerNum && !a.isNumber)
                return Times(Rational(IntegerNum.ONE, b), a).eval()
        }

        if (a.isNumber && b.isNumber)
            return a.asNumber() / b.asNumber()

        return this
    }

    override fun toString(): String = if (size == 2) get(0).toString() + " / " + get(1) else super.toString()

    override fun validate() {
        validateParameterCount(2)
    }
}
