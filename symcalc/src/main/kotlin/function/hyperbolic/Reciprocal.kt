package org.cerion.symcalc.function.hyperbolic

import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.function.arithmetic.Divide


class Sech(e: Any): HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        return Divide(1, Cosh(e)).eval(ctx)
    }
}

class Csch(e: Any): HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        return Divide(1, Sinh(e)).eval(ctx)
    }
}

class Coth(e: Any): HyperbolicBase(e) {
    override fun evaluate(e: Expr): Expr {
        return Divide(1, Tanh(e)).eval(ctx)
    }
}
