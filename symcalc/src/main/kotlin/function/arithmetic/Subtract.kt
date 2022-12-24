package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr

class Subtract(vararg e: Any) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.LISTABLE.value

    public override fun evaluate(): Expr {
        // a + (-1*b)
        return get(0) + Times(-1, get(1))
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
