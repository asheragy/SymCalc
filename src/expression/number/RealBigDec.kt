package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.core.N
import org.nevec.rjm.BigDecimalMath
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.min

class RealBigDec(override val value: BigDecimal) : NumberExpr() {

    constructor(value: String) : this(BigDecimal(value))
    constructor(value: Double) : this(BigDecimal(value))

    override val numType: NumberType get() = NumberType.REAL_BIGDEC
    override val isZero: Boolean get() = value.compareTo(BigDecimal.ZERO) == 0
    override val isOne: Boolean get() = value == BigDecimal.ONE
    override val isNegative: Boolean get() = value.signum() == -1
    override val precision: Int get() = value.precision()

    fun toDouble(): Double = value.toDouble()
    override fun toString(): String = "$value`$precision"
    override fun unaryMinus(): RealBigDec = RealBigDec(value.negate())

    override fun compareTo(other: NumberExpr): Int {
        if (other is RealBigDec) {
            return value.compareTo(other.value)
        }

        return evaluate(SYSTEM_DECIMAL_PRECISION).compareTo(other.evaluate(SYSTEM_DECIMAL_PRECISION))
    }

    override fun plus(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER -> {
                val bigDec = other.asInteger().toBigDecimal()
                return RealBigDec( this.value.plus(bigDec))
            }
            NumberType.RATIONAL -> {
                val n = N(other, Integer(precision)).eval()
                return this + n as NumberExpr
            }
            NumberType.REAL_DOUBLE -> return other.asDouble() + this

            NumberType.REAL_BIGDEC -> {
                other as RealBigDec

                // TODO_LP issue with zero and precision, this is just a workaround until more is learned on how it
                if (isZero)
                    return other
                if (other.isZero)
                    return this

                val min = min(this.precision, other.precision)
                val a = this.evaluate(min) as RealBigDec
                val b = other.evaluate(min) as RealBigDec

                return RealBigDec( a.value.plus(b.value))
            }
            NumberType.COMPLEX -> {
                return other + this
            }
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        when(other.numType) {
            NumberType.INTEGER -> {
                other as Integer
                val n = BigDecimal(other.value)
                return RealBigDec(value.times(n))
            }

            NumberType.RATIONAL -> {
                other as Rational
                val n = this * other.numerator
                return n / other.denominator
            }
            NumberType.REAL_DOUBLE -> return other.asDouble() * this
            NumberType.REAL_BIGDEC -> {
                other as RealBigDec

                // Both are BigDecimal
                val bd = this.value.times(other.value)
                val result = RealBigDec(bd)
                return evaluatePrecision(result, this, other)
            }

            NumberType.COMPLEX -> {
                other as Complex
                return Complex(this * other.real, this * other.img)
            }
        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER -> {
                other as Integer
                val n = BigDecimal(other.value)
                return RealBigDec(value.divide(n, MathContext(precision, RoundingMode.HALF_UP)))
            }

            NumberType.RATIONAL -> {
                other as Rational
                return (this * other.denominator) / other.numerator
            }
            NumberType.REAL_DOUBLE -> return RealDouble(toDouble() / other.asDouble().value)
            NumberType.REAL_BIGDEC -> {
                other as RealBigDec
                val result = RealBigDec( this.value.divide(other.value, RoundingMode.HALF_UP))
                return evaluatePrecision(result, this, other)
            }

            NumberType.COMPLEX -> {
                return Complex(this) / other
            }
        }
    }

    fun pow(other: RealBigDec): NumberExpr {
        val bigDec = BigDecimalMath.pow(value, other.value)
        return RealBigDec(bigDec)
    }

    override fun evaluate(precision: Int): NumberExpr {
        if (precision == SYSTEM_DECIMAL_PRECISION)
            return RealDouble(toDouble())
        else if (precision < this.precision) {
            val leftDigits = value.precision() - value.scale()
            val newScale = precision - leftDigits
            if (newScale == 0)
                return this

            return RealBigDec(value.setScale(newScale, RoundingMode.HALF_UP))
        }

        return this
    }

    private fun evaluatePrecision(result: RealBigDec, a: RealBigDec, b: RealBigDec): RealBigDec {
        val min = min(a.value.precision(), b.value.precision())
        if (min == 0 && a.isZero || b.isZero)
            return result

        return RealBigDec(result.value.round(MathContext(min, RoundingMode.HALF_UP)))
    }
}
