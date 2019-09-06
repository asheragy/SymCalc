package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr

class ComplexInfinity : ConstExpr() {

    // TODO_LP add all cases of trig functions for this, when they are added

    override fun evaluate(precision: Int): Expr {
        return this
    }

    override fun evaluate(): Expr {
        return this
    }

    override fun toString(): String = "ComplexInfinity"
}