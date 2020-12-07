package org.cerion.symcalc.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberType

class GCD(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val a = get(0) as Integer
        val b = get(1) as Integer

        val gcd = a.gcd(b)

        if (size == 2)
            return gcd

        val next = mutableListOf<Expr>(gcd)
        for(i in 2 until size)
            next.add(args[i])

        return GCD(*next.toTypedArray()).eval()
    }

    @Throws(ValidationException::class)
    override fun validate() {
        for (i in 0 until size)
            validateNumberType(i, NumberType.INTEGER)
    }
}
