package org.cerion.symcalc.function.trig

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Divide
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec
import kotlin.math.tan

class Sec(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        val cos = Cos(get(0)).eval()

        if (cos is Cos)
            return this

        return Divide(1, cos).eval()
    }
}

class Csc(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        val sin = Sin(get(0)).eval()
        if (sin is Sin)
            return this

        return Divide(1, sin).eval()
    }
}

/* TODO this should be able to replace other class
class Cot(e: Any) : FunctionExpr(e) {
    override fun evaluate(): Expr {
        val tan = Tan(get(0)).eval()
        if (tan is Tan)
            return this

        val a = Divide(1, tan).eval()
        return a
    }
}
 */

class Cot(e: Any): TrigBase(e), StandardTrigFunction {
    override fun evaluateAsDouble(d: Double): Double = 1 / tan(d)

    override fun evaluate(e: Expr): Expr {
        return this
    }

    override fun evaluatePiFactoredOut(e: Expr): Expr {
        // Pi * Integer = 0
        if (e is Integer)
            return ComplexInfinity()

        if (e is Rational) {
            val ratio =
                when(e.denominator) {
                    Integer((2)) -> Integer.ZERO
                    Integer(3) -> Tan.oneOverSqrt3
                    Integer(4) -> Integer.ONE
                    Integer(6) -> Tan.sqrt3
                    else -> return this
                }

            val mod = e.numerator % (Integer.TWO * e.denominator)
            val position = mod.intValue().toDouble() / (e.denominator.intValue() * 2)
            if (position in 0.250001..0.50 || position in 0.750001..1.0)
                return Minus(ratio).eval()

            return ratio
        }

        return this // Could not evaluate
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): RealBigDec {
        return Divide(Cos(x), Sin(x)).eval() as RealBigDec
    }
}