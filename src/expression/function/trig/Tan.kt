package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealNum
import org.cerion.symcalc.expression.number.RealNum_Double
import kotlin.math.tan

class Tan(vararg e: Expr) : TrigBase(Function.TAN, *e) {
    override fun evaluateAsDouble(d: Double): Expr = RealNum_Double(tan(d))

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun evaluate(e: Expr): Expr {
        return this
    }
}