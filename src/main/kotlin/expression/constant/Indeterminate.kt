package org.cerion.symcalc.expression.constant

import org.cerion.symcalc.expression.ConstExpr

class Indeterminate : ConstExpr() {
    override fun toString(): String = "Indeterminate"
}