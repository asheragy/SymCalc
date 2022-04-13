package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.number.RealDouble
import kotlin.math.log10

class Log10(vararg e: Any) : FunctionExpr(*e) {

    override fun evaluate(): Expr {
        val n = get(0)

        if (n is RealDouble)
            return RealDouble(log10(n.value))

        return this
    }
}