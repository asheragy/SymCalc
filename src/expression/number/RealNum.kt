package org.cerion.symcalc.expression.number

abstract class RealNum : NumberExpr() {

    //val isDouble: Boolean get() = this is RealNum_Double
    override val numType: NumberType get() = NumberType.REAL

    // TODO this might be able to be replaced as well
    abstract fun toDouble(): Double
}