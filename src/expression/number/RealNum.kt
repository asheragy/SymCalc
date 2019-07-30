package org.cerion.symcalc.expression.number

import java.math.BigDecimal

abstract class RealNum : NumberExpr() {

    abstract val isWholeNumber: Boolean // TODO better name for this? isInteger() is already taken
    abstract fun toInteger(): IntegerNum

    val isDouble: Boolean get() = this is RealNum_Double

    override val numType: NumberType get() = NumberType.REAL

    override fun canExp(other: NumberExpr): Boolean {
        return other.numType !== NumberType.COMPLEX
    }

    companion object {

        fun create(s: String) : RealNum {
            val value = java.lang.Double.parseDouble(s)

            if (value.toString().length < s.length)
                return RealNum_BigDecimal(BigDecimal(s))

            return RealNum_Double(value)
        }

        fun create(n: IntegerNum): RealNum = RealNum_Double(n)
        fun create(n: Double): RealNum = RealNum_Double(n)

        fun create(bigDecimal: BigDecimal): RealNum = RealNum_BigDecimal(bigDecimal)
    }
}