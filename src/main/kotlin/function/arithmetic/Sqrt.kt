package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.expression.number.Rational

// TODO replace some usages of this (specifically BigDecimal) with a .sqrt()
class Sqrt(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        return Power(get(0), Rational(1, 2)).eval()
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
