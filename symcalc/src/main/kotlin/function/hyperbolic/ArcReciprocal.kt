package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.constant.ComplexInfinity
import org.cerion.symcalc.constant.Indeterminate
import org.cerion.symcalc.constant.Infinity
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.*
import org.cerion.symcalc.number.Integer
import org.cerion.symcalc.number.Rational


class ArcSech(e: Any): HyperbolicBase(e) {
    override fun evaluate(z: Expr): Expr {
        if (z is ComplexInfinity)
            return Indeterminate()

        val recip = Divide(1, z)
        val eval = Log(Sqrt(recip - 1) * Sqrt(recip + 1) + recip).eval()
        if (eval is Log)
            return this

        return eval
    }
}

class ArcCsch(e: Any): HyperbolicBase(e) {
    override fun evaluate(z: Expr): Expr {
        if (z == Integer.ZERO)
            return ComplexInfinity()

        val sqrt = Sqrt(Divide(1, Power(z, 2)) + 1)
        val eval = Log(sqrt + (Divide(1, z))).eval()
        if (eval is Log)
            return this

        return eval
    }
}

class ArcCoth(e: Any): HyperbolicBase(e) {
    override fun evaluate(z: Expr): Expr {
        when (z) {
            is Infinity -> return Integer.ZERO
            is ComplexInfinity -> return Integer.ZERO
        }

        val eval = Log(z + 1) - Log(z - 1)
        if (eval !is Subtract)
            return Rational.HALF * eval

        return this
    }
}