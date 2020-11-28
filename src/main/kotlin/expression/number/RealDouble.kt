package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.constant.I
import org.cerion.symcalc.expression.AtomExpr
import org.cerion.symcalc.expression.constant.Pi
import org.cerion.symcalc.expression.function.arithmetic.*
import kotlin.math.pow
import kotlin.math.roundToInt

class RealDouble(override val value: Double = 0.0) : NumberExpr(), AtomExpr {

    override val type: ExprType get() = ExprType.NUMBER
    override val numType: NumberType get() = NumberType.REAL_DOUBLE
    override val isZero: Boolean get() = value == 0.0
    override val isOne: Boolean get() = value == 1.0
    override val isNegative: Boolean get() = value < 0
    override val precision: Int get() = MachinePrecision

    override fun toString(): String = "" + value

    override fun toPrecision(precision: Int): NumberExpr {
        return this // Cannot upgrade precision so do nothing
    }

    override fun compareTo(other: NumberExpr): Int {
        return when(other) {
            is Integer,
            is Rational -> this.compareTo(other.toPrecision(MachinePrecision))
            is RealDouble -> value.compareTo(other.value)
            is RealBigDec -> value.compareTo(other.toDouble())
            is Complex -> Complex(this).compareTo(other)
            else -> throw NotImplementedError()
        }
    }

    override fun unaryMinus(): RealDouble = RealDouble(0 - value)

    override fun plus(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer,
            is Rational -> this + other.toPrecision(MachinePrecision)
            is RealDouble -> RealDouble(value + other.value)
            is RealBigDec -> RealDouble(value + other.toDouble())
            is Complex -> Complex(this) + other
            else -> throw NotImplementedError()
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer,
            is Rational -> this * other.toPrecision(MachinePrecision)
            is RealDouble -> RealDouble(value * other.value)
            is RealBigDec -> RealDouble(value * other.toDouble())
            is Complex -> Complex(this) * other
            else -> throw NotImplementedError()
        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer,
            is Rational -> this / other.toPrecision(MachinePrecision)
            is RealDouble -> RealDouble(value / other.value)
            is RealBigDec -> RealDouble(value / other.toDouble())
            is Complex -> Complex(this) / other
            else -> throw NotImplementedError()
        }
    }

    override fun pow(other: NumberExpr): NumberExpr {
        when (other) {
            is Integer,
            is Rational -> return this.pow(other.toPrecision(precision))
            is RealDouble -> {
                val pow = value.pow(other.value)
                if (pow.isNaN() && value < 0)
                    return Exp(Times(other, Log(this.unaryMinus()) +  Times(I(), Pi()))).eval() as NumberExpr

                return RealDouble(pow)
            }
            is RealBigDec -> return RealDouble(value.pow(other.toDouble()))
            is Complex -> return Power(this, other).eval() as NumberExpr
            else -> throw NotImplementedError()
        }
    }

    override fun quotient(other: NumberExpr): NumberExpr {
        return when (other) {
            is Complex -> (this / other).round()
            else -> (this / other).floor()
        }
    }

    override fun round(): Integer = Integer(value.roundToInt())
    override fun floor(): Integer = Integer(kotlin.math.floor(value).toInt())
}
