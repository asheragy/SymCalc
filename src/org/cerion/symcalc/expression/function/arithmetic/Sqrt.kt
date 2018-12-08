package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.RationalNum

class Sqrt(vararg e: Expr) : FunctionExpr(Function.SQRT, *e) {

    override fun evaluate(): Expr {
        return Power(get(0), RationalNum(1, 2)).eval()
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}