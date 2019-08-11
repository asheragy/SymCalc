package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.*
import org.cerion.symcalc.expression.function.integer.Mod
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealNum
import org.cerion.symcalc.expression.number.RealNum_Double
import kotlin.math.cos

class Cos(vararg e: Expr) : TrigBase(Function.COS, *e) {
    override fun evaluateAsDouble(d: Double): Expr = RealNum_Double(cos(d))

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        if (e is IntegerNum) {
            if (e.isOdd)
                return IntegerNum.NEGATIVE_ONE
            return IntegerNum.ONE
        }

        if (e is Rational) {
            var offset = IntegerNum.ZERO

            val ratio =
            when(e.denominator) {
                IntegerNum((2)) -> IntegerNum.ZERO
                IntegerNum(3) -> {
                    offset = IntegerNum.ONE
                    Rational.HALF
                }
                IntegerNum(4) -> {
                    offset = IntegerNum.TWO
                    oneOverSqrt2
                }
                IntegerNum(6) -> {
                    offset = IntegerNum(3)
                    sqrt3Over2
                }
                else -> return this
            }

            val mod = Mod(Plus(e.numerator, offset), Times(IntegerNum.TWO, e.denominator).eval().asInteger()).eval().asInteger().intValue()

            if (mod >= e.denominator.intValue())
                return Minus(ratio).eval()

            return ratio
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

    companion object {
        val sqrt3Over2 = Times(Rational(1,2), Power(IntegerNum(3), Rational(1,2)))
        val oneOverSqrt2 = Power(IntegerNum.TWO, Rational(-1,2))
    }
}
