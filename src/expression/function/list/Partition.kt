package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberType

class Partition(vararg e: Expr) : FunctionExpr(Function.PARTITION, *e) {

    override fun evaluate(): Expr {

        val input = get(0) as ListExpr
        val n = (get(1) as Integer).intValue()
        val result = ListExpr()

        for (i in 0 until input.size / n) {
            val sublist = ListExpr()

            for (j in 0 until n) {
                val pos = i * n + j
                if (pos >= input.size)
                    break

                sublist.add(input[pos])
            }

            result.add(sublist)
        }

        return result
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(2)
        validateParameterType(0, ExprType.LIST)
        validateNumberType(1, NumberType.INTEGER)
    }

}
