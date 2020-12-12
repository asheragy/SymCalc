package org.cerion.symcalc.function.special

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.constant.E
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Power
import org.nevec.rjm.BigDecimalMath

class Gamma(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {

        val z = get(0)
        if (z is RealBigDec) {
            // Reduce using Gamma(x) = Gamma(x+1) / x
            if (z.isNegative)
                return Gamma(z + 1) / z

            if (z.toDouble() > 1.5) {
                val n = (z.toDouble() - 0.5).toInt()
                val xsmall = (z - n) as RealBigDec
                val bigDec = xsmall.forcePrecision(RealBigDec.getStoredPrecision(z.precision))
                val poch = RealBigDec(BigDecimalMath.pochhammer(bigDec, n))
                return Gamma(xsmall) * poch
            }

            //val t = evalIntegrationByParts(z)
            //return t

            val t = z.forcePrecision(RealBigDec.getStoredPrecision(z.precision))
            return RealBigDec(BigDecimalMath.Gamma(t), z.precision)
        }

        return this
    }

    private fun evalIntegrationByParts(z: RealBigDec): Expr {
        // Integration by parts formula by splitting on [0,x] and [x,infinity]
        // The 2nd part is small enough to ignore for large enough x
        // TODO calculate a better x default to use here
        val x = Integer(25)
        val xe = Power(x, z) * Power(E(), x.unaryMinus()) // x^z * e^-x
        var term = Integer(1) / z // term at N=0
        var sum = term

        // Infinite sum of x^n / z*(z+1)...(z+n)
        for(n in 1 until 100) {
            term *= x
            term /= z + Integer(n)

            val t = sum + term
            if (t == sum)
                return t * xe

            sum = t
        }

        throw IterationLimitExceeded()
    }
}