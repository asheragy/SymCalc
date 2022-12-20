package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Divide


class Sech(e: Any): HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        return Divide(1, Cosh(e)).eval()
    }
}

class Csch(e: Any): HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        return Divide(1, Sinh(e)).eval()
    }
}

class Coth(e: Any): HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        return Divide(1, Tanh(e)).eval()
    }
}