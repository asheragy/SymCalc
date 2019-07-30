package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RationalNum
import org.cerion.symcalc.expression.number.RealNum
import kotlin.math.cos

class Cos(vararg e: Expr) : TrigBase(Function.COS, *e) {
    override fun evaluateAsDouble(d: Double): Expr = RealNum.create(cos(d))

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        if (e == IntegerNum.ONE)
            return IntegerNum.NEGATIVE_ONE

        if (e.isInteger && e.asInteger().isEven)
            return IntegerNum.ONE

        if (e.isNumber && e.asNumber().isRational) {
            e as RationalNum
            if (e.denominator == IntegerNum.TWO)
                return IntegerNum.ZERO
        }

        return this
    }

    override fun evaluate(e: Expr): Expr {

        // Even*Pi = 1 / Odd*Pi = -1
        if (e is Times && e.args.any { it is Pi } && e.args.any { it is IntegerNum }) {
            val removeIntegers = Times()
            e.args.forEach {
                if (it !is IntegerNum)
                    removeIntegers.add(it)
            }
            val cos = Cos(removeIntegers)

            val theInteger = e.args.first { it is IntegerNum } as IntegerNum
            if (theInteger.isEven)
                return Times(IntegerNum.NEGATIVE_ONE, cos).eval()

            return cos.eval()
        }

        // Pi / Integer
        if (e is Divide && e.args[1] == IntegerNum.TWO) {
            return IntegerNum.ZERO
        }

        return this
    }
}
