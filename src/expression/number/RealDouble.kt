package org.cerion.symcalc.expression.number

class RealDouble(override val value: Double = 0.0) : NumberExpr() {

    override val numType: NumberType get() = NumberType.REAL_DOUBLE
    override val isZero: Boolean get() = value == 0.0
    override val isOne: Boolean get() = value == 1.0
    override val isNegative: Boolean get() = value < 0
    override val precision: Int get() = SYSTEM_DECIMAL_PRECISION

    override fun toString(): String = "" + value

    override fun evaluate(precision: Int): NumberExpr {
        return this // Cannot upgrade precision so do nothing
    }

    override fun compareTo(other: NumberExpr): Int {
        when(other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL -> return this.compareTo(other.evaluate(SYSTEM_DECIMAL_PRECISION))
            NumberType.REAL_DOUBLE -> return value.compareTo(other.asDouble().value)
            NumberType.REAL_BIGDEC -> return value.compareTo(other.asBigDec().toDouble())
            NumberType.COMPLEX -> return Complex(this).compareTo(other)

        }
    }

    override fun unaryMinus(): RealDouble = RealDouble(0 - value)

    override fun plus(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL -> return this + other.evaluate(SYSTEM_DECIMAL_PRECISION)
            NumberType.REAL_DOUBLE -> return RealDouble(value + other.asDouble().value)
            NumberType.REAL_BIGDEC -> return RealDouble(value + other.asBigDec().toDouble())
            NumberType.COMPLEX -> return Complex(this) + other
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL -> return this * other.evaluate(SYSTEM_DECIMAL_PRECISION)
            NumberType.REAL_DOUBLE -> return RealDouble(value * other.asDouble().value)
            NumberType.REAL_BIGDEC -> return RealDouble(value * other.asBigDec().toDouble())
            NumberType.COMPLEX -> return Complex(this) * other

        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER,
            NumberType.RATIONAL -> return this / other.evaluate(SYSTEM_DECIMAL_PRECISION)
            NumberType.REAL_DOUBLE -> return RealDouble(value / other.asDouble().value)
            NumberType.REAL_BIGDEC -> return RealDouble(value / other.asBigDec().toDouble())
            NumberType.COMPLEX -> return Complex(this) / other
        }
    }
}
