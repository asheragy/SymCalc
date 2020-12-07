package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.ComplexInfinity
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr

class Subtract(vararg e: Expr) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.LISTABLE.value

    public override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        //Identity
        if (b is NumberExpr && b.isZero)
            return a

        if (a is NumberExpr && b is NumberExpr)
            return a - b

        if (a is ComplexInfinity || b is ComplexInfinity)
            return ComplexInfinity()

        return this
    }

    override fun toString(): String {
        if (size == 2) {
            val e2 = get(1)
            return get(0).toString() + " - " + if (e2.isFunction("subtract")) "($e2)" else e2
        }

        return super.toString()
    }

    override fun validate() {
        validateParameterCount(2)
    }
}
