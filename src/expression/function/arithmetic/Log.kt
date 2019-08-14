package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealNum_Double
import kotlin.math.ln

class Log(vararg e: Expr) : FunctionExpr(Function.LOG, *e) {
    override fun validate() {
        validateParameterCount(1)
    }

    override fun evaluate(): Expr {
        val n = get(0)

        if (n.isNumber) {
            n as NumberExpr
            if (n is RealNum_Double)
                return RealNum_Double(ln(n.value))
        }

        if (n is E)
            return IntegerNum.ONE

        return this
    }

}