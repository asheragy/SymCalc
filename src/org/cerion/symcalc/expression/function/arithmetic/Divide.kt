package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr

class Divide(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.DIVIDE, *e) {

    override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        //Identity
        if (b.isNumber && b.asNumber().isOne)
            return a

        if (a.isNumber && b.isNumber)
            return a.asNumber() / b.asNumber()

        return this
    }

    override fun toString(): String = if (size() == 2) get(0).toString() + " / " + get(1) else super.toString()

    override fun validate() {
        validateParameterCount(2)
    }
}
