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
    override fun evaluateAsDouble(d: Double): Double {
        TODO("Not yet implemented")
    }

    override fun evaluate(e: Expr): Expr {
        TODO("Not yet implemented")
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return RealBigDec(x.value.arcsinh(x.maxStoredPrecision), x.precision)
    }
}

class ArcCosh(e: Expr) : HyperbolicBase(e) {
    override fun evaluateAsDouble(d: Double): Double {
        TODO("Not yet implemented")
    }

    override fun evaluate(e: Expr): Expr {
        TODO("Not yet implemented")
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        if (x < RealBigDec("1.0")) {
            TODO("Add complex result")
        }

        return RealBigDec(x.value.arccosh(x.maxStoredPrecision), x.precision)
    }
}

class ArcTanh(e: Expr) : HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        // TODO https://mathworld.wolfram.com/InverseHyperbolicTangent.html
        if(e is Integer) {
            when(e) {
                // TODO should be directed infinity
                Integer.NEGATIVE_ONE -> return Minus(Infinity())
                Integer.ZERO -> return Integer.ZERO
                Integer.ONE -> return Infinity()
            }
        }

        if(e is Infinity)
            return Times(Complex(0, Rational.HALF.unaryMinus()), Pi())

        return this
    }

    override fun evaluateAsDouble(d: Double): Double {
        TODO("Not yet implemented")
    }

    override fun evaluateAsBigDecimal(x: RealBigDec): Expr {
        return Rational.HALF * Log((x + 1) / (x.unaryMinus() + 1))
    }

}