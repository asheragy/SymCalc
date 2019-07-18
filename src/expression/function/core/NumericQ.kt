package org.cerion.symcalc.expression.function.core

import org.cerion.symcalc.exception.ValidationException
import org.cerion.symcalc.expression.BoolExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr

class NumericQ(vararg e: Expr) : FunctionExpr(Function.NUMERICQ, *e) {

    override fun evaluate(): Expr {
        val test = get(0)

        if (test.isNumber)
            return BoolExpr.TRUE

        if (test.isConst)
            return BoolExpr.TRUE

        if (test.isFunction) {
            val func = test as FunctionExpr
            if (func.isNumeric) {

                for (i in 0 until func.size()) {
                    val isNumeric = NumericQ(func[i]).eval() as BoolExpr
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
