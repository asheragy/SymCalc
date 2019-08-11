package org.cerion.symcalc.expression.number

import expression.number.BigDecimalMath
import org.cerion.symcalc.expression.function.core.N
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.min

class RealNum_BigDecimal(override val value: BigDecimal) : RealNum() {

    constructor(value: String) : this(BigDecimal(value))
    constructor(value: Double) : this(BigDecimal(value))

    override val isZero: Boolean get() = value == BigDecimal.ZERO
    override val isOne: Boolean get() = value == BigDecimal.ONE
    override val isNegative: Boolean get() = value.signum() == -1
    override val precision: Int get() = value.precision()

    override fun toDouble(): Double = value.toDouble()
    override fun toString(): String = "$value`$precision"
    override fun unaryMinus(): RealNum_BigDecimal = RealNum_BigDecimal(value.negate())

    override fun compareTo(other: NumberExpr): Int {
        if (other.isReal && other.asReal() !is RealNum_Double) {
            val bigDec = other.asReal() as RealNum_BigDecimal
            return value.compareTo(bigDec.value)
        }

        return toDouble().compareTo(other.toDouble())
    }

    override fun plus(other: NumberExpr): NumberExpr {
        when (other.numType) {
            NumberType.INTEGER -> {
                val bigDec = other.asInteger().toBigDecimal()
                return RealNum_BigDecimal( this.value.plus(bigDec))
            }
            NumberType.RATIONAL -> {
                val n = N(other, precision).eval()
                return this + n as NumberExpr
            }
            NumberType.REAL -> {
                val real = other.asReal()
                if(real is RealNum_Double)
                    return real + this

                real as RealNum_BigDecimal

                // Both are BigDecimal
                val min = min(this.precision, other.precision)
                val a = this.evaluate(min) as RealNum_BigDecimal
                val b = other.evaluate(min) as RealNum_BigDecimal

                return RealNum_BigDecimal( a.value.plus(b.value))
            }
            NumberType.COMPLEX -> {
                return other + this
            }
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        when(other.numType) {
            NumberType.INTEGER -> {
                other as IntegerNum
                val n = BigDecimal(other.value)
                return RealNum_BigDecimal(value.times(n))
            }

            NumberType.RATIONAL -> {
                other as Rational
                val n = this * other.numerator
                return n / other.denominator
            }

            NumberType.REAL -> {
                val real = other.asReal()
                if(real is RealNum_Double)
                    return real * this

                real as RealNum_BigDecimal

                // Both are BigDecimal
                val bd = this.value.times(real.value)
                val result = RealNum_BigDecimal(bd)
                return evaluatePrecision(result, this, real)
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
                other as IntegerNum
                val n = BigDecimal(other.value)
                return RealNum_BigDecimal(value.divide(n, MathContext(precision, RoundingMode.HALF_UP)))
            }

            NumberType.RATIONAL -> {
                other as Rational
                return (this * other.denominator) / other.numerator
            }

            NumberType.REAL -> {
                val real = other.asReal()
                if(real is RealNum_Double) {
                    return RealNum_Double(toDouble() / real.value)
                }

                real as RealNum_BigDecimal

                // Both are BigDecimal
                val result = RealNum_BigDecimal( this.value.divide(real.value, RoundingMode.HALF_UP))
                return evaluatePrecision(result, this, real)
            }

            NumberType.COMPLEX -> {
                return Complex(this) / other
            }
        }
    }

    fun pow(other: RealNum_BigDecimal): NumberExpr {
        val bigDec = BigDecimalMath.pow(value, other.value)
        return RealNum_BigDecimal(bigDec)
    }

    override fun evaluate(precision: Int): NumberExpr {
        if (precision == SYSTEM_DECIMAL_PRECISION)
            return create(toDouble())
        else if (precision < this.precision) {
            val leftDigits = value.precision() - value.scale()
            val newScale = precision - leftDigits
            if (newScale == 0)
                return this

            return RealNum_BigDecimal(value.setScale(newScale, RoundingMode.HALF_UP))
        }

        return this
    }

    private fun evaluatePrecision(result: RealNum_BigDecimal, a: RealNum_BigDecimal, b: RealNum_BigDecimal): RealNum_BigDecimal {
        val min = min(a.value.precision(), b.value.precision())
        if (min == 0 && a.isZero || b.isZero)
            return result

        return RealNum_BigDecimal(result.value.round(MathContext(min, RoundingMode.HALF_UP)))
    }
}
