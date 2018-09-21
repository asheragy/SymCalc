package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.NumberExpr
import org.cerion.symcalc.expression.number.RealNum

class Tan(e: Expr) : TrigBase(FunctionType.TAN, e) {

    override fun evaluate(num: NumberExpr?): Expr {
        if (num != null && !num.isComplex) {
            return RealNum.create(Math.tan(num.toDouble()))
        }

        return this
    }
}