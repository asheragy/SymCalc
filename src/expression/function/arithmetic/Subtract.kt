package org.cerion.symcalc.expression.function.arithmetic

import expression.toList
import org.cerion.symcalc.expression.ErrorExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function

class Subtract(vararg e: Expr) : FunctionExpr(Function.SUBTRACT, *e) {

    public override fun evaluate(): Expr {
        val a = get(0)
        val b = get(1)

        //Identity
        if (b.isNumber && b.asNumber().isZero)
            return a

        if (a.isNumber && b.isNumber)
            return a.asNumber() - b.asNumber()

        if (a.isList || b.isList) {
            if (a.isList && b.isList) {
                if (a.size == b.size) {
                    val result = ListExpr()
                    for (i in 0 until a.size) {
                        var e: Expr = Subtract(a[i], b[i])
                        e = e.eval()
                        result.add(e)
                    }

                    return result
                }

                return ErrorExpr("list sizes not equal")
            } else return if (a.isList)
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
