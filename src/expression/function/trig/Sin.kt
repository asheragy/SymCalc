package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealNum
import kotlin.math.sin

class Sin(vararg e: Expr) : TrigBase(Function.SIN, *e) {

    override fun evaluate(num: NumberExpr): Expr {
        return if (!num.isComplex) RealNum.create(sin(num.toDouble())) else this

    }

}
