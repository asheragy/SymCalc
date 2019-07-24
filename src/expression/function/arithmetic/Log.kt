package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.RealNum
import kotlin.math.ln

class Log(vararg e: Expr) : FunctionExpr(Function.LOG, *e) {

    override fun evaluate(): Expr {

        if (get(0).isNumber && env.isNumericalEval) {
            val n = get(0).asNumber()

            return RealNum.create(ln(n.toDouble()))
        }

        return this
    }

}