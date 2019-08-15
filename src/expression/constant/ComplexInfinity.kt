package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr

class ComplexInfinity : ConstExpr() {

    override fun evaluate(precision: Int): Expr {
        return this
    }

    override fun evaluate(): Expr {
        return this
    }

    override fun toString(): String = "ComplexInfinity"
}