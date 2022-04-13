package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.constant.E
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.number.*
import org.cerion.symcalc.function.FunctionExpr
import kotlin.math.ln

// TODO replace more calls to this to use RealBigDec.log whenever possible
class Log(vararg e: Any) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.LISTABLE.value

    override fun validate() {
        validateParameterCount(1)
    }

    override fun evaluate(): Expr {
        val n = get(0)

        if (n is NumberExpr) {
            if (n.isNegative)
                return PI_I + Log(n.unaryMinus())

            if (n is Rational && n < Integer.ONE)
                return Times(Integer.NEGATIVE_ONE, Log(n.reciprocal()))
            if (n is RealDouble)
                return RealDouble(ln(n.value))
            if (n is RealBigDec)
                return n.log()
        }

        if (n is E)
            return Integer.ONE

        if (n is Power && n.args[0] is E)
            return n.args[1]

        return this
    }

    private companion object {
        val PI_I = Times(Complex(0, 1), Pi())
    }

}