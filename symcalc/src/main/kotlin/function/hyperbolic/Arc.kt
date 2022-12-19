package org.cerion.symcalc.function.hyperbolic

import org.cerion.math.bignum.decimal.arccosh
import org.cerion.math.bignum.decimal.arcsinh
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Minus
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational
import org.cerion.symcalc.number.RealBigDec

class ArcSinh(e: Expr) : HyperbolicBase(e) {
    override fun evaluate(x: Expr): Expr {
        when(x) {
            is RealBigDec -> return RealBigDec(x.value.arcsinh(x.maxStoredPrecision), x.precision)
        }

        return this
    }
}

class ArcCosh(e: Expr) : HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        when(e) {
            is RealBigDec -> {
                if (e < RealBigDec("1.0")) {
                    TODO("Add complex result")
                }

                return RealBigDec(e.value.arccosh(e.maxStoredPrecision), e.precision)
            }
        }

        return this
    }
}

class ArcTanh(e: Expr) : HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        // TODO https://mathworld.wolfram.com/InverseHyperbolicTangent.html

        when(e) {
            is Integer -> {
                when(e.value.toInt()) {
                    // TODO should be directed infinity
                    -1 -> return Minus(Infinity())
                    0 -> return Integer.ZERO
                    1 -> return Infinity()
                }
            }
            is RealBigDec -> return Rational.HALF * Log((e + 1) / (e.unaryMinus() + 1))
            is Infinity -> return Times(Complex(0, Rational.HALF.unaryMinus()), Pi())
        }

        return this
    }
}