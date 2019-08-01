package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.Environment
import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealNum
import java.math.BigDecimal
import java.math.RoundingMode

class Pi : ConstExpr() {
    override fun toString(): String = "Pi"

    override fun evaluate(): Expr {
        if (isNumericalEval) {
            if (precision == SYSTEM_DECIMAL_PRECISION)
                return RealNum.create(Math.PI)

            return RealNum.create( getPiToDigits(precision) )
        }
        else
            return this
    }

    private fun getPiToDigits(n: Int): BigDecimal {
        var sum = calc(0, n+2)

        // TODO change loop limit to something that makes sense
        for(i in 1..10000) {
            val prev = sum
            sum += calc(i, n+2)

            if (sum == prev)
                return sum.setScale(n, RoundingMode.HALF_UP)
        }

        // TODO this means we did not calculate exactly...
        return sum
    }

    // 16-k [ 4/(8k+1) - 2/(8k+4) - 1/(8k+5) - 1/(8k+6) ]
    private fun calc(i: Int, scale: Int): BigDecimal {
        var k = BigDecimal(i).setScale(scale)
        val k8 = k.times(BigDecimal(8.0))

        var result = BigDecimal(4.0).setScale(scale) / k8.plus(BigDecimal.ONE)
        result -= BigDecimal(2.0).setScale(scale) / k8.plus(BigDecimal(4.0))
        result -= BigDecimal(1.0).setScale(scale) / k8.plus(BigDecimal(5.0))
        result -= BigDecimal(1.0).setScale(scale) / k8.plus(BigDecimal(6.0))

        k = BigDecimal(16).pow(i)
        k = BigDecimal.ONE.divide(k)
        result *= k

        return result.setScale(scale, RoundingMode.HALF_UP)
    }
}