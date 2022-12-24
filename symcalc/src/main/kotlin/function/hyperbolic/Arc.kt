package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.minus
import org.cerion.symcalc.function.arithmetic.Log
import org.cerion.symcalc.function.arithmetic.Power
import org.cerion.symcalc.function.arithmetic.Sqrt
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.number.Complex
import org.cerion.symcalc.number.Rational

class ArcSinh(e: Any) : HyperbolicBase(e) {
    override fun evaluate(z: Expr): Expr {
        if (z is ComplexInfinity)
            return ComplexInfinity()

        val eval = Log(Sqrt(Power(z, 2) + 1) + z).eval()
        if (eval is Log)
            return this

        return eval
    }
}

class ArcCosh(e: Any) : HyperbolicBase(e) {
    override fun evaluate(z: Expr): Expr {
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
            is Infinity -> return Times(Complex(0, Rational.HALF.unaryMinus()), Pi())
        }

        val eval = Rational.HALF * (Log(z + 1) - Log(1 - z))
        if (eval is Log)
            return this

        return eval
    }
}