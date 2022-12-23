package org.cerion.symcalc.function.hyperbolic

import org.cerion.math.bignum.decimal.arccosh
import org.cerion.math.bignum.decimal.arcsinh
import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.minus
import org.cerion.symcalc.function.arithmetic.*
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec

class ArcSinh(e: Any) : HyperbolicBase(e) {
    override fun evaluate(z: Expr): Expr {
        when(z) {
            is ComplexInfinity -> return ComplexInfinity()
            // TODO is this necessary to define there?
            is RealBigDec -> return RealBigDec(z.value.arcsinh(z.maxStoredPrecision), z.precision)
        }

        val eval = Log(Sqrt(Power(z, 2) + 1) + z).eval()
        if (eval is Log)
            return this

        return eval
    }
}

class ArcCosh(e: Any) : HyperbolicBase(e) {
    override fun evaluate(z: Expr): Expr {
        when(z) {
            is RealBigDec -> {
                if (z < RealBigDec("1.0")) {
                    TODO("Add complex result")
                }

                return RealBigDec(z.value.arccosh(z.maxStoredPrecision), z.precision)
            }
        }

        val eval = Log(z + Sqrt(z - 1) * Sqrt(z + 1)).eval()
        if (eval is Log)
            return this

        return eval
    }
}

class ArcTanh(e: Any) : HyperbolicBase(e) {
    override fun evaluate(z: Expr): Expr {
        // TODO https://mathworld.wolfram.com/InverseHyperbolicTangent.html

        when(z) {
            is Integer -> {
                when(z.value.toInt()) {
                    // TODO should be directed infinity
                    -1 -> return Minus(Infinity())
                    0 -> return Integer.ZERO
                    1 -> return Infinity()
                }
            }
            is Infinity -> return Times(Complex(0, Rational.HALF.unaryMinus()), Pi())
        }

        val eval = Rational.HALF * (Log(z + 1) - Log(1 - z))
        if (eval is Log)
            return this

        return eval
    }
}