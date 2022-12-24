package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberExpr

class Subtract(vararg e: Any) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.LISTABLE.value

    public override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        //Identity
        if (b is NumberExpr && b.isZero)
            return a

        if (a is Integer && a.isZero)
            return b * -1

        when(a) {
            is NumberExpr -> {
                if (b is NumberExpr)
                    return a - b
            }
            is Infinity -> {
                if (b is Infinity)
                    return Indeterminate()
                if (b is NumberExpr || b.eval(MachinePrecision) is NumberExpr)
                    return Infinity()
            }
        }

        if (a is ComplexInfinity || b is ComplexInfinity)
            return ComplexInfinity()

        if (b is Infinity && (a is NumberExpr || a.eval(MachinePrecision) is NumberExpr))
            return Infinity(-1)

        // TODO most of the above should be able to just return this
        //return a + Times(-1, b)

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
