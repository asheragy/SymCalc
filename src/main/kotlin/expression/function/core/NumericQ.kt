package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.NumberExpr

class NumericQ(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val test = get(0)

        if (test is NumberExpr)
            return BoolExpr.TRUE

        if (test is ConstExpr)
            return BoolExpr.TRUE

        if (test is FunctionExpr) {
            if (test.isNumeric) {

                for (i in 0 until test.size) {
                    val isNumeric = NumericQ(test[i]).eval() as BoolExpr
                    if (isNumeric === BoolExpr.FALSE)
                        return BoolExpr.FALSE
                }

                return BoolExpr.TRUE
            }
        }

        return BoolExpr.FALSE
    }

    @Throws(ValidationException::class)
    override fun validate() {
        validateParameterCount(1)
    }
}
