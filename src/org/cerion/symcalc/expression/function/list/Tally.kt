package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.arithmetic.Plus
import org.cerion.symcalc.expression.number.IntegerNum

class Tally(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.TALLY, *e) {

    override fun evaluate(): Expr {
        val result = ListExpr()

        val list = getList(0)
        for (i in 0 until list.size()) {
            val e = list.get(i)

            var found = false
            for (j in 0 until result.size()) {
                var keyval = result.get(j)
                val (key, value) = Pair(keyval.get(0), keyval.get(1).asInteger())

                if (e.equals(key)) {
                    val v2 = Plus(value, IntegerNum.ONE).eval().asInteger()
                    keyval = ListExpr(e, v2)
                    result.set(j, keyval)
                    found = true
                    break
                }
            }

            if (!found)
                result.add(ListExpr(e, IntegerNum.ONE))
        }

        return result
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, Expr.ExprType.LIST)
    }
}
