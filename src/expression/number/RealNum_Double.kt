package org.cerion.symcalc.expression.number

import kotlin.math.floor

internal class RealNum_Double(override val value: Double = 0.0) : RealNum() {

    override val isZero: Boolean get() = value == 0.0
    override val isOne: Boolean get() = value == 1.0
    override val isNegative: Boolean get() = value < 0
    override val precision: Int get() = SYSTEM_DECIMAL_PRECISION

    val isWholeNumber: Boolean
        get() = value == floor(value) && !java.lang.Double.isInfinite(value)

    override fun toDouble(): Double = value
    override fun toString(): String = "" + value

    override fun evaluate(precision: Int): NumberExpr {
        return this // Cannot upgrade precision so do nothing
    }

    override fun compareTo(other: NumberExpr): Int {
        when(other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL -> return this.compareTo(other.evaluate(SYSTEM_DECIMAL_PRECISION))
            NumberType.REAL -> return toDouble().compareTo(other.asReal().toDouble())
            NumberType.COMPLEX -> return Complex(this).compareTo(other)
        }
    }

    override fun unaryMinus(): RealNum_Double = RealNum_Double(0 - value)

    override fun plus(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL -> return this + other.evaluate(SYSTEM_DECIMAL_PRECISION)
            NumberType.REAL -> return RealNum_Double(value + other.asReal().toDouble())
            NumberType.COMPLEX -> return Complex(this) + other
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL -> return this * other.evaluate(SYSTEM_DECIMAL_PRECISION)
            NumberType.REAL -> return RealNum_Double(value * other.asReal().toDouble())
            NumberType.COMPLEX -> return Complex(this) * other
        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL -> return this / other.evaluate(SYSTEM_DECIMAL_PRECISION)
            NumberType.REAL -> return RealNum_Double(value / other.asReal().toDouble())
            NumberType.COMPLEX -> return Complex(this) / other
        }
    }
}
