package org.cerion.symcalc.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.ListExpr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.NumberType

class IdentityMatrix(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val n = getInteger(0).intValue()
        val result = mutableListOf<Expr>()

        for (i in 0 until n) {
            val sublist = mutableListOf<Integer>()
            for (j in 0 until n)
                sublist.add(if (i == j) Integer.ONE else Integer.ZERO)

            result.add(ListExpr(sublist))
        }

        return ListExpr(result)
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
        validateParameterType(0, ExprType.NUMBER)
        validateNumberType(0, NumberType.INTEGER)

        val n = getInteger(0).intValue()
        if (n <= 0)
            throw ValidationException("Number must be a positive integer")
    }
}
