package org.cerion.symcalc.expression.function.trig

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.Function
import org.cerion.symcalc.expression.function.arithmetic.Divide
import org.cerion.symcalc.expression.function.arithmetic.Times
import org.cerion.symcalc.expression.number.IntegerNum
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.RealNum
import kotlin.math.sin

class Sin(vararg e: Expr) : TrigBase(Function.SIN, *e) {
    override fun evaluate(e: Expr): Expr {
        // TODO add tests for multiples of Pi
        if (e is Pi)
            return IntegerNum.ZERO

        // Any Integer * Pi = 0
        if (e is Times && e.args.any { it is Pi } && e.args.any { it is IntegerNum }) {
            val removeIntegers = Times()
            e.args.forEach {
                if (it !is IntegerNum)
                    removeIntegers.add(it)
            }

            return Sin(removeIntegers).eval()
        }

        // Pi / Integer
        if (e is Divide && e.args[1] == IntegerNum.TWO) {
            return IntegerNum.ONE
        }

        return this
    }

    override fun evaluate(num: NumberExpr): Expr {
        return if (!num.isComplex) RealNum.create(sin(num.toDouble())) else this
    }

}
