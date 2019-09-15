package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import java.math.BigDecimal
import java.math.RoundingMode

class E : ConstExpr() {

    override fun toString(): String = "E"

    override fun evaluate(): Expr {
        return evaluate(InfinitePrecision)
    }

    override fun evaluate(precision: Int): Expr {
        if (precision < InfinitePrecision) {
            if (precision == MachinePrecision)
                return RealDouble(Math.E)

            return RealBigDec( getEToDigits(precision) )
        }
        else
            return this
    }

    private fun getEToDigits(n: Int): BigDecimal {
        var e: BigDecimal = BigDecimal.ONE.setScale(n+1)
        var next: BigDecimal = BigDecimal.ONE.setScale(n+1)

        var i = 2
        while (next.compareTo(BigDecimal.ZERO) != 0) {
            e += next
            next = next.divide(BigDecimal(i).setScale(n+1), RoundingMode.HALF_UP)
            i++
        }

        return e.setScale(n-1, RoundingMode.HALF_UP)
    }
}