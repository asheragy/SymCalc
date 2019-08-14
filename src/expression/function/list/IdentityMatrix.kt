package org.cerion.symcalc.expression.function.list

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.*
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberType

class IdentityMatrix(vararg e: Expr) : FunctionExpr(Function.IDENTITY_MATRIX, *e) {

    override fun evaluate(): Expr {
        val n = getInteger(0).intValue()
        val result = ListExpr()

        for (i in 0 until n) {
            val sublist = ListExpr()
            for (j in 0 until n)
                sublist.add(if (i == j) IntegerNum.ONE else IntegerNum.ZERO)

            result.add(sublist)
        }

        return result
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
