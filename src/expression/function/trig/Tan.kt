package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealNum
import kotlin.math.tan

class Tan(e: Expr) : TrigBase(Function.TAN, e) {
    override fun evaluate(e: Expr): Expr {
        return this
    }

    override fun evaluate(num: NumberExpr): Expr {
        if (!num.isComplex) {
            return RealNum.create(tan(num.toDouble()))
        }

        return this
    }
}