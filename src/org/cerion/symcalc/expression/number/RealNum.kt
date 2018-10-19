package org.cerion.symcalc.expression.number

abstract class RealNum : NumberExpr() {

    abstract val isWholeNumber: Boolean // TODO better name for this? isInteger() is already taken
    abstract fun toInteger(): IntegerNum

    override val numType: NumberType get() = NumberType.REAL

    override fun canExp(num: NumberExpr): Boolean {
        return if (num.numType === NumberType.COMPLEX) false else true
    }

    companion object {
        fun create(s: String) : RealNum = RealNum_Double(s)
        fun create(n: IntegerNum): RealNum = RealNum_Double(n)
        fun create(r: RationalNum): RealNum = RealNum_Double(r)
        fun create(n: Double): RealNum = RealNum_Double(n)
    }
}