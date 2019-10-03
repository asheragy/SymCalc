package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer

class Tally(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val result = mutableListOf<Expr>()

        val list = getList(0)
        for (i in 0 until list.size) {
            val e = list[i]

            var found = false
            for (j in 0 until result.size) {
                var keyval = result[j] as ListExpr
                val (key, value) = Pair(keyval[0], keyval[1].asInteger())

                if (e.equals(key)) {
                    val v2 = value + Integer.ONE
                    keyval = ListExpr(e, v2)
                    result[j] = keyval
                    found = true
                    break
                }
            }

            if (!found)
                result.add(ListExpr(e, Integer.ONE))
        }

        return ListExpr(result)
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.LIST)
    }
}
