package org.cerion.symcalc.expression.function.integer

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.FunctionExpr
import org.cerion.symcalc.expression.NumberExpr
import org.cerion.symcalc.expression.number.IntegerNum

class GCD(vararg e: Expr) : FunctionExpr(FunctionExpr.FunctionType.GCD, *e) {

    override fun evaluate(): Expr {
        //TODO can take more than 2 integer parameters
        val a = get(0) as IntegerNum
        val b = get(1) as IntegerNum

        return a.gcd(b)
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(0)
        validateNumberType(0, NumberExpr.INTEGER)
        validateNumberType(1, NumberExpr.INTEGER)
    }
}
