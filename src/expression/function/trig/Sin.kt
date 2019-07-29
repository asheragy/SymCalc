package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Minus
import org.cerion.symcalc.expression.function.arithmetic.Power
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.function.integer.Mod
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RationalNum
import org.cerion.symcalc.expression.number.RealNum
import kotlin.math.sin

class Sin(vararg e: Expr) : TrigBase(Function.SIN, *e) {

    override fun evaluate(num: NumberExpr): Expr {
        return if (!num.isComplex) RealNum.create(sin(num.toDouble())) else this
    }

    override fun evaluate(e: Expr): Expr {
        if (e is Pi || (e.isNumber && e.asNumber().isZero))
            return IntegerNum.ZERO

        if (e is Times) {
            val index = e.args.indexOfFirst { it is Pi }
            val times = Times()
            for (i in 0 until e.args.size)
                if (i != index)
                    times.add(e.args[i])

            return evaluatePiFactoredOut(times.eval())
        }

        if (e is Divide && e.args[0] is Pi) {
            return evaluatePiFactoredOut(Divide(IntegerNum.ONE, e.args[1]).eval())
        }

        return this // Could not evaluate
    }

    private fun evaluatePiFactoredOut(e: Expr): Expr {
        // Pi * Integer = 0
        if (e is IntegerNum)
            return IntegerNum.ZERO

        if (e is RationalNum) {
            val mod = Mod(e.numerator, Times(IntegerNum.TWO, e.denominator).eval().asInteger()).eval().asInteger().intValue()
            when(e.denominator) {
                IntegerNum((2)) -> {
                    if (mod == 1)
                        return IntegerNum.ONE
                    else
                        return IntegerNum.NEGATIVE_ONE
                }
                IntegerNum(3) -> {
                    if(mod == 1 || mod == 2)
                        return sqrt3Over2
                    else
                        return Minus(sqrt3Over2).eval()
                }
                IntegerNum(4) -> {
                    if(mod == 1 || mod == 3)
                        return oneOverSqrt2
                    else
                        return Minus(oneOverSqrt2).eval()
                }
                IntegerNum(6) -> {
                    if (mod == 1 || mod == 5)
                        return RationalNum(1,2)
                    else
                        return RationalNum(-1,2)
                }
            }
        }

        return this // Could not evaluate
    }

    companion object {
        val sqrt3Over2 = Times(RationalNum(1,2), Power(IntegerNum(3), RationalNum(1,2)))
        val oneOverSqrt2 = Power(IntegerNum.TWO, RationalNum(-1,2))
    }
}
