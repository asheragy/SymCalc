package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.expression.number.RealDouble
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class Pi : ConstExpr() {
    override fun toString(): String = "Pi"

    override fun evaluate(): Expr {
        return evaluate(InfinitePrecision)
    }

    override fun evaluate(precision: Int): Expr {
        if (precision < InfinitePrecision) {
            if (precision == SYSTEM_DECIMAL_PRECISION)
                return RealDouble(Math.PI)

            return RealBigDec( getPiToDigits(precision) )
        }
        else
            return this
    }

    private var one: BigDecimal = BigDecimal.ZERO
    private var two: BigDecimal = BigDecimal.ZERO
    private var four: BigDecimal = BigDecimal.ZERO

    private fun getPiToDigits(scale: Int): BigDecimal {
        // Initialize
        one = BigDecimal(1.0).setScale(scale)
        two = BigDecimal(2.0).setScale(scale)
        four = BigDecimal(4.0).setScale(scale)

        var sum = calc(0, scale)

        // FEAT look into another algorithm this one is slow after 2000+ digits
        for(i in 1..10000) {
            val prev = sum
            sum += calc(i, scale)

            if (sum == prev) {
                return sum.setScale(scale - 1, RoundingMode.HALF_UP)
            }
        }

        throw RuntimeException("too many iterations required to calculate")
    }

    // 16-k [ 4/(8k+1) - 2/(8k+4) - 1/(8k+5) - 1/(8k+6) ]
    private fun calc(i: Int, scale: Int): BigDecimal {
        var k = BigDecimal(i).setScale(scale)
        val k8 = k.times(BigDecimal(8.0))

        var result = four / k8.plus(BigDecimal.ONE)
        result -= two / k8.plus(BigDecimal(4.0))
        result -= one / k8.plus(BigDecimal(5.0))
        result -= one / k8.plus(BigDecimal(6.0))

        k = BigDecimal(16).pow(i)
        k = BigDecimal.ONE.divide(k)
        result *= k

        return result.setScale(scale, RoundingMode.HALF_UP)
    }
}
