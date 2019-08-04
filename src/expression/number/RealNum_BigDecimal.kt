package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.core.N
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.min
import kotlin.math.pow

class RealNum_BigDecimal(override val value: BigDecimal) : RealNum() {

    constructor(value: String) : this(BigDecimal(value))

    override val isWholeNumber: Boolean get() = throw NotImplementedError()
    override val isZero: Boolean get() = value == BigDecimal.ZERO
    override val isOne: Boolean get() = value == BigDecimal.ONE
    override val isNegative: Boolean get() = value.signum() == -1
    override val precision: Int get() = value.precision()

    override fun toInteger(): IntegerNum = IntegerNum(value.toLong())
    override fun toDouble(): Double = value.toDouble()
    override fun toString(): String = value.toString()
    override fun unaryMinus(): RealNum_BigDecimal = RealNum_BigDecimal(value.negate())

    override fun compareTo(other: NumberExpr): Int {
        if (other.isReal && !other.asReal().isDouble) {
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
                if(real.isDouble)
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
                other as RationalNum
                val n = this * other.numerator
                return n / other.denominator
            }

            NumberType.REAL -> {
                val real = other.asReal()
                if(real.isDouble)
                    return real * this

                real as RealNum_BigDecimal

                // Both are BigDecimal
                val bd = this.value.times(real.value)
                val result = RealNum_BigDecimal(bd)
                return evaluatePrecision(result, this, real)
            }

            NumberType.COMPLEX -> {
                other as ComplexNum
                return ComplexNum(this * other.real, this * other.img)
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
                other as RationalNum
                return (this * other.denominator) / other.numerator
            }

            NumberType.REAL -> {
                var real = other.asReal()
                if(real.isDouble) {
                    real = RealNum_BigDecimal(BigDecimal(real.toDouble()))
                    //return create(toDouble() / real.toDouble())
                }

                real as RealNum_BigDecimal

                // Both are BigDecimal
                val result = RealNum_BigDecimal( this.value.divide(real.value, RoundingMode.HALF_UP))
                return evaluatePrecision(result, this, real)
            }

            NumberType.COMPLEX -> {
                return ComplexNum(this) / other
            }
        }
    }

    override fun evaluate(precision: Int): NumberExpr {
        if (precision == SYSTEM_DECIMAL_PRECISION)
            return create(toDouble())
        else if (precision < this.precision) {
            val leftDigits = value.precision() - value.scale()
            return RealNum_BigDecimal(value.setScale(precision - leftDigits, RoundingMode.HALF_UP))
        }

        return this
    }

    private fun evaluatePrecision(result: RealNum_BigDecimal, a: RealNum_BigDecimal, b: RealNum_BigDecimal): RealNum_BigDecimal {
        val min = min(a.value.precision(), b.value.precision())
        return RealNum_BigDecimal(result.value.round(MathContext(min, RoundingMode.HALF_UP)))
    }
}
