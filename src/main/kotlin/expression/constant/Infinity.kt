package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr

class Infinity : ConstExpr() {
    override fun evaluate(precision: Int): Expr = this
    override fun evaluate(): Expr = this
    override fun toString(): String = "Infinity"
}