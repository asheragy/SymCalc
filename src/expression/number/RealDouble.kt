package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.AtomExpr

class RealDouble(override val value: Double = 0.0) : NumberExpr(), AtomExpr {

    override val numType: NumberType get() = NumberType.REAL_DOUBLE
    override val isZero: Boolean get() = value == 0.0
    override val isOne: Boolean get() = value == 1.0
    override val isNegative: Boolean get() = value < 0
    override val precision: Int get() = MachinePrecision

    override fun toString(): String = "" + value

    override fun evaluate(precision: Int): NumberExpr {
        return this // Cannot upgrade precision so do nothing
    }

    override fun compareTo(other: NumberExpr): Int {
        when(other) {
            is Integer,
            is Rational -> return this.compareTo(other.evaluate(MachinePrecision))
            is RealDouble -> return value.compareTo(other.value)
            is RealBigDec -> return value.compareTo(other.toDouble())
            is Complex -> return Complex(this).compareTo(other)
            else -> throw NotImplementedError()
        }
    }

    override fun unaryMinus(): RealDouble = RealDouble(0 - value)

    override fun plus(other: NumberExpr): NumberExpr {
        when (other) {
            is Integer,
            is Rational -> return this + other.evaluate(MachinePrecision)
            is RealDouble -> return RealDouble(value + other.value)
            is RealBigDec -> return RealDouble(value + other.toDouble())
            is Complex -> return Complex(this) + other
            else -> throw NotImplementedError()
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        when (other) {
            is Integer,
            is Rational -> return this * other.evaluate(MachinePrecision)
            is RealDouble -> return RealDouble(value * other.value)
            is RealBigDec -> return RealDouble(value * other.toDouble())
            is Complex -> return Complex(this) * other
            else -> throw NotImplementedError()
        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        when (other) {
            is Integer,
            is Rational -> return this / other.evaluate(MachinePrecision)
            is RealDouble -> return RealDouble(value / other.value)
            is RealBigDec -> return RealDouble(value / other.toDouble())
            is Complex -> return Complex(this) / other
            else -> throw NotImplementedError()
        }
    }
}
