package org.cerion.symcalc.constant

import org.cerion.symcalc.expression.ConstExpr

open class Infinity(val direction: Int) : ConstExpr() {
    constructor() : this(1)
    override fun toString(): String = if(direction < 0) "-∞" else "∞"
}
