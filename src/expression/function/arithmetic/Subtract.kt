package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.number.NumberExpr

class Subtract(vararg e: Expr) : FunctionExpr(Function.SUBTRACT, *e) {

    public override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        //Identity
        if (b is NumberExpr && b.isZero)
            return a

        if (a is NumberExpr && b is NumberExpr)
            return a - b

        if (a is ListExpr || b is ListExpr) {
            if (a is ListExpr && b is ListExpr) {
                if (a.size == b.size) {
                    return ListExpr(*a.args.mapIndexed { index, expr -> Subtract(expr, b[index]).eval() }.toTypedArray())
                }

                return ErrorExpr("list sizes not equal")
            } else return if (a is ListExpr)
                Subtract(a, b.toList(a.size)).eval()
            else
                Subtract(a.toList(b.size), b).eval()
        }

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
