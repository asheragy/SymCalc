package org.cerion.symcalc.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.number.Complex

class I : ConstExpr() {
    override fun toString(): String = "i"

    override fun evaluateInfinitePrecision() = Complex(0, 1)
    override fun evaluateMachinePrecision() = Complex(0.0, 1.0)
}