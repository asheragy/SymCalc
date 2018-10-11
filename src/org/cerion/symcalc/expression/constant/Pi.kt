package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr
import org.cerion.symcalc.expression.number.RealNum

class Pi : ConstExpr() {
    override fun toString(): String = "Pi"
    override fun evaluate(): Expr = if (env.isNumericalEval) RealNum.create(Math.PI) else this
}
