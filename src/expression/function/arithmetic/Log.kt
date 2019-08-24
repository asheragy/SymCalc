package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import org.nevec.rjm.BigDecimalMath
import kotlin.math.ln

class Log(vararg e: Expr) : FunctionExpr(Function.LOG, *e) {
    override fun validate() {
        validateParameterCount(1)
    }

    override fun evaluate(): Expr {
        val n = get(0)

        if (n is NumberExpr) {
            if (n is RealDouble)
                return RealDouble(ln(n.value))
            if (n is RealBigDec) {
                val log = BigDecimalMath.log(n.value)
                return RealBigDec(log)
            }
        }

        if (n is E)
            return Integer.ONE

        return this
    }

}