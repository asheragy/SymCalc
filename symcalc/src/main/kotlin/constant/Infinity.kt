package org.cerion.symcalc.constant

import org.cerion.symcalc.expression.ConstExpr
import org.cerion.symcalc.expression.Expr

open class Infinity(val direction: Int) : ConstExpr() {
    constructor() : this(1)
    override fun toString(): String = if(direction < 0) "-∞" else "∞"

    operator fun plus(other: Infinity): Expr {
        return if (direction > 0 && other.direction > 0)
            Infinity()
        else if (direction < 0 && other.direction < 0)
            Infinity(-1)
        else
            Indeterminate()
    }
}
