package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.ConstExpr

class ComplexInfinity : ConstExpr() {

    // TODO_LP add all cases of trig functions for this, when they are added

    override fun toString(): String = "ComplexInfinity"
}