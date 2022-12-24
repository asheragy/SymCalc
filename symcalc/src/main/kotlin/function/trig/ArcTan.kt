package org.cerion.symcalc.function.trig

import org.cerion.math.bignum.decimal.arctan
import org.cerion.symcalc.constant.*
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.*
import org.cerion.symcalc.number.*

class ArcTan(vararg e: Any) : TrigBase(*e) {

    override fun evaluateAsDouble(d: Double): Double = kotlin.math.atan(d)

    override fun evaluate(e: Expr): Expr {
        when (e) {
            is Integer -> {
                if (e.isOne)
                    return Pi() / 4
                else if (e == Integer.NEGATIVE_ONE)
                    return Pi() / -4
                else if (e.isZero)
                    return Integer.ZERO
            }
            is Complex -> {
                val log = Log(Subtract(1, I() * e)) - Log(Plus(1, I() * e)).eval()
                return Rational.HALF * I() * log
            }
            is Infinity -> return Divide(Pi(), Integer(2))
            is ComplexInfinity -> return Indeterminate()
            is Power -> {
                if (e[0] == Integer(3)) {
                    if (e[1] == Rational(-1, 2))
                        return Divide(Pi(), Integer(6))
                    else if (e[1] == Rational.HALF)
                        return Divide(Pi(), Integer(3))
                }
            }
            is Times -> {
                if (e[0] == Rational.THIRD && e[1] == Power(Integer(3), Rational.HALF))
                    return Divide(Pi(), Integer(6))
            }
        }

        return this
    }

    fun evaluate(x: NumberExpr, y: NumberExpr): Expr {
        if (x.isZero) {
            if (y.isZero)
                return Indeterminate()

            val piOver2 = Pi() / 2
            return if (y.isNegative)
                Minus(piOver2).eval()
            else
                piOver2
        }

        val result = ArcTan(y / x)
        if (x.isNegative) {
            if (y.isNegative)
                return result - Pi()
            else
                return result + Pi()
        }
        else
            return result.eval()
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return RealBigDec(x.value.arctan(x.maxStoredPrecision), x.precision)
    }
}