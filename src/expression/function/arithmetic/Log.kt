package org.cerion.symcalc.expression.function.arithmetic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.FunctionExpr
import org.cerion.symcalc.expression.number.*
import org.nevec.rjm.BigDecimalMath
import kotlin.math.ln

class Log(vararg e: Expr) : FunctionExpr(Function.LOG, *e) {

    override val properties: Int
        get() = Properties.LISTABLE.value

    override fun validate() {
        validateParameterCount(1)
    }

    override fun evaluate(): Expr {
        val n = get(0)

        if (n is NumberExpr) {
            if (n.isNegative) {
                // TODO maybe can generalize all of this after checking the rational case
                if (n is Integer)
                    return Plus(PI_I, Log(n.unaryMinus()))
                if (n is Rational) {
                    if (n > Integer.NEGATIVE_ONE)
                        return Plus(PI_I, Times(Integer(-1), Log(n.unaryMinus())))
                    else
                        return Plus(PI_I, Log(n.unaryMinus()))
                }

                return Plus(PI_I, Log(n.unaryMinus())).eval()
            }

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

    private companion object {
        val PI_I = Times(Complex(0, 1), Pi())
    }

}