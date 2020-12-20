package org.cerion.symcalc.function.special

import org.cerion.symcalc.constant.E
import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.Integer
import org.cerion.symcalc.expression.number.NumberExpr
import org.cerion.symcalc.expression.number.Rational
import org.cerion.symcalc.expression.number.RealBigDec
import org.cerion.symcalc.function.FunctionExpr
import org.cerion.symcalc.function.arithmetic.Exp
import org.cerion.symcalc.function.arithmetic.Power

class Gamma(vararg e: Expr) : FunctionExpr(*e) {

    override fun evaluate(): Expr {

        val z = get(0)
        if (z is RealBigDec) {
            // Reduce using Gamma(x) = Gamma(x+1) / x
            if (z.isNegative)
                return Gamma(z + 1) / z

            /*
            if (z.toDouble() > 1.5) {
                val n = (z.toDouble() - 0.5).toInt()
                val xsmall = (z - n) as RealBigDec
                val bigDec = xsmall.forcePrecision(RealBigDec.getStoredPrecision(z.precision))
                val poch = RealBigDec(BigDecimalMath.pochhammer(bigDec, n))
                return Gamma(xsmall) * poch
            }

             */

            //val t = evalIntegrationByParts(z)
            //return t

            val t = approximate(z)
            return t

            //val t = z.forcePrecision(RealBigDec.getStoredPrecision(z.precision))
            //return RealBigDec(BigDecimalMath.Gamma(t), z.precision)
        }

        return this
    }

    // Lanczos approximation
    private fun approximate(input: RealBigDec): RealBigDec {
        val z = (input - Integer.ONE) as RealBigDec
        val g = Integer(5) // TODO how to set this based on input

        val term = Power(z + g + Rational.HALF, z + Rational.HALF).eval()
        val exp = Exp((z + g + Rational.HALF).unaryMinus()).eval()
        val ag = A(g, z)

        val t = (Integer(2) * term * exp * ag) as RealBigDec
        return t
    }

    private fun A(g: Integer, z: RealBigDec): RealBigDec {
        var sum = Rational.HALF * P(g, 0)
        var term: NumberExpr = Integer.ONE

        for (i in 1..g.intValue() + 2) {
            term *= (z - Integer(i - 1))
            term /= (z + Integer(i))
            sum += P(g, i) * term
        }

        return sum as RealBigDec
    }

    private fun P(g: Integer, k: Int): Expr {
        var sum: Expr = Integer.ZERO

        for(i in 0..k) {
            val c = C(2*k+1, 2*i+1)
            val term = Power(Integer(i) + g + Rational.HALF, (Integer(i) + Rational.HALF).unaryMinus())
            val exp = Exp(Integer(i) + g + Rational.HALF)

            // Factorial starting at -1/2 is Gamma(1/2) incrementing by 1
            // Sqrt(Pi) cancels out in final value so calculate factorial without it
            // n = i + 1/2
            // n/2! = (n - 2)!! / n^(n-1 / 2)
            val n = ((Integer(i) + Rational.HALF) as Rational).numerator
            var factorial: NumberExpr = Integer.ONE

            if (i > 0) {
                for (ii in n.intValue()-2 downTo 1 step 2)
                    factorial *= Integer(ii)

                val div = (Power(Integer(2), (n - 1) / Integer(2)).eval() as NumberExpr)
                factorial /= div
            }

            //println(c)

            sum += Integer(c) * factorial * term * exp
        }

        return sum
    }

    //private var counter = 0
    private fun C(n: Int, m: Int): Long {
        //counter++
        //println("$counter")
        if (m == n) {
            if (n <= 2)
                return 1L

            when (n) {
                3 -> return 2
                4 -> return 4
                5 -> return 8
                6 -> return 16
                7 -> return 32
                8 -> return 64
                9 -> return 128
                10 -> return 256
                11 -> return 512
            }
        }

        if (m == 1)
            return -C(n-2, m)

        if (n > m)
            return 2 * C(n-1, m-1) - C(n-2, m)

        val result = 2 * C(n-1, m-1)
        //println("$n $m $result")
        return result
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