package org.cerion.symcalc.function.arithmetic

import org.cerion.symcalc.constant.*
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.trig.ArcTan
import org.cerion.symcalc.number.*
import kotlin.math.ln


class Log(vararg e: Any) : FunctionExpr(*e) {

    override val properties: Int
        get() = Properties.Listable.value

    override fun validate() {
        validateParameterCount(1)
    }

    override fun evaluate(): Expr {
        val n = get(0)

        when(n) {
            is NumberExpr -> {
                if (n !is Complex && n.isNegative)
                    return PI_I + Log(n.unaryMinus())

                when(n) {
                    is RealDouble -> return RealDouble(ln(n.value))
                    is RealBigDec -> return n.log()
                    is Integer -> {
                        if(n == Integer.ONE)
                            return Integer.ZERO
                        else if (n == Integer.ZERO)
                            return Infinity(-1)
                    }
                    is Rational -> {
                        if (n < Integer.ONE)
                            return Times(-1, Log(n.reciprocal()))
                    }
                    is Complex -> return Log(n.abs()) + I() * ArcTan(n.real, n.img).eval()
                }
            }
            is E -> return Integer.ONE
            is Infinity -> return Infinity()
            is ComplexInfinity -> return Infinity()
            is Power -> {
                if (n.args[0] is E)
                    return n.args[1]
            }
        }

        return this
    }

    private companion object {
        val PI_I = Times(Complex(0, 1), Pi())
    }

}