package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberType
import org.cerion.symcalc.expression.number.Integer

class GCD(vararg e: Expr) : FunctionExpr(Function.GCD, *e) {

    override fun evaluate(): Expr {
        val a = get(0) as Integer
        val b = get(1) as Integer

        val gcd = a.gcd(b)

        if (size == 2)
            return gcd

        val next = GCD(gcd)
        for(i in 2 until size)
            next.add(args[i])

        return next.eval()
    }

    @Throws(ValidationException::class)
    override fun validate() {
        for (i in 0 until size)
            validateNumberType(i, NumberType.INTEGER)
    }
}
