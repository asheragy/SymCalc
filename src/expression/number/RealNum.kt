package org.cerion.symcalc.expression.number

import java.math.BigDecimal

abstract class RealNum : NumberExpr() {

    //val isDouble: Boolean get() = this is RealNum_Double
    override val numType: NumberType get() = NumberType.REAL

    // TODO this might be able to be replaced as well
    abstract fun toDouble(): Double

    companion object {
        fun create(s: String) : RealNum {
            val value = java.lang.Double.parseDouble(s)

            if (value.toString().length < s.length)
                return RealNum_BigDecimal(BigDecimal(s))

            return RealNum_Double(value)
        }

        @Deprecated("use constructor directly", ReplaceWith("RealNum_Double(n)", "org.cerion.symcalc.expression.number.RealNum_Double"))
        fun create(n: Double): RealNum = RealNum_Double(n)
    }
}