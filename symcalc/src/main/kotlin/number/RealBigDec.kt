package org.cerion.symcalc.number

import org.cerion.math.bignum.decimal.*
import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.expression.AtomExpr
import org.cerion.symcalc.function.arithmetic.Exp
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.core.N
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class RealBigDec(override val value: BigDecimal, override val precision: Int) : NumberExpr(), AtomExpr {

    companion object {
        val ZERO = RealBigDec(BigDecimal("0.0"))

        private const val MaxExtraPrecisionBase2_32 = 1
        fun getStoredPrecision(desiredPrecision: Int): Int {
            return (((desiredPrecision * 0.10381025296523008) + 1 + MaxExtraPrecisionBase2_32).toInt() * 9.632959861247397).toInt()
        }
    }

    val maxStoredPrecision
        get() = getStoredPrecision(precision)

    constructor(value: BigDecimal) : this(value, value.precision())
    constructor(value: String) : this(BigDecimal(value))
    constructor(value: String, precision: Int) : this(BigDecimal(value), precision)

    override val type: ExprType get() = ExprType.NUMBER
    override val numType: NumberType get() = NumberType.REAL_BIGDEC
    override val isZero: Boolean get() = value.compareTo(BigDecimal.ZERO) == 0
    override val isOne: Boolean get() = value.compareTo(BigDecimal.ONE) == 0
    override val isNegative: Boolean get() = value.signum() == -1
    val accuracy: Int get() = value.scale()

    fun toDouble(): Double = value.toDouble()
    fun isWholeNumber() = this.toDouble() == kotlin.math.round(this.toDouble())
    fun toInteger() = Integer(value.toBigInteger())
    override fun floor(): Integer = Integer(value.setScale(0, RoundingMode.FLOOR).toBigInteger())
    override fun round(): Integer = Integer(value.setScale(0, RoundingMode.HALF_UP).toBigInteger())

    private fun getRepresentedValue(): BigDecimal {
        if (precision < value.precision())
            return value.round(MathContext(precision, RoundingMode.HALF_UP))
        else if (precision > value.precision())
            return value.setScale(precision - (value.precision() - value.scale()), RoundingMode.HALF_UP)

        return value
    }

    override fun toString(): String = "${getRepresentedValue().toPlainString()}`$precision"
    override fun unaryMinus(): RealBigDec = RealBigDec(value.negate(), precision)

    override fun compareTo(other: NumberExpr): Int {
        if (other is RealBigDec)
            return getRepresentedValue().compareTo(other.getRepresentedValue())

        if (other is RealDouble)
            return value.toDouble().compareTo(other.value)

        val real = other.toPrecision(precision) as RealBigDec
        return value.compareTo(real.value)
    }

    override fun plus(other: NumberExpr): NumberExpr {
        when (other) {
            is Integer -> {
                val bigDec = other.toBigDecimal()
                return RealBigDec( this.value.plus(bigDec), precision)
            }
            is Rational -> {
                val n = N(other, Integer(precision)).eval()
                return this + n as NumberExpr
            }
            is RealDouble -> return other + this
            is RealBigDec -> {
                if (isZero)
                    return other
                if (other.isZero)
                    return this

                val desiredPrecision = min(this.precision, other.precision)
                val result = value.add(other.value, MathContext(getStoredPrecision(desiredPrecision)))
                return RealBigDec(result, desiredPrecision)
            }
            is Complex -> return other + this
        }
    }

    operator fun times(other: RealBigDec): RealBigDec {
        // Limit precision to whatever the maxed stored value would be
        val expectedPrecision = value.precision() + other.value.precision()
        val maxPrecision = getStoredPrecision(max(precision, other.precision))
        val mc = MathContext(min(expectedPrecision, maxPrecision), RoundingMode.HALF_UP)

        val bd = this.value.multiply(other.value, mc)
        return RealBigDec(bd, min(precision, other.precision))
    }

    override fun times(other: NumberExpr): NumberExpr {
        return when(other) {
            is Integer -> RealBigDec(value.times(other.value.toBigDecimal()), precision)
            is Rational -> (this * other.numerator) / other.denominator
            is RealDouble -> other * this
            is RealBigDec -> return this * other
            is Complex -> Complex(this * other.real, this * other.img)
        }
    }

    operator fun div(other: RealBigDec): RealBigDec {
        val newPrecision = min(precision, other.precision)
        val mc = MathContext(getStoredPrecision(newPrecision), RoundingMode.HALF_UP)
        return RealBigDec( this.value.divide(other.value, mc), newPrecision)
    }

    override fun div(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer -> RealBigDec(value.divide(other.value.toBigDecimal(), MathContext(getStoredPrecision(precision), RoundingMode.HALF_UP)), precision)
            is Rational -> (this * other.denominator) / other.numerator
            is RealDouble -> RealDouble(toDouble() / other.value)
            is RealBigDec -> this / other
            is Complex -> Complex(this) / other
        }
    }

    fun pow(other: RealBigDec): RealBigDec {
        val precision = min(precision, other.precision)
        val storedPrecision = getStoredPrecision(precision)
        return RealBigDec(value.pow(other.value, storedPrecision), precision)
    }

    override infix fun pow(other: NumberExpr): NumberExpr {
        when (other) {
            is Integer -> {
                if (other.isOne)
                    return this

                val number = value.pow(other.intValue(), MathContext(getStoredPrecision(precision), RoundingMode.HALF_UP))
                return RealBigDec(number, precision)
            }
            is Rational -> {
                // ^(b/2)
                if (!other.isNegative && other.denominator == Integer.TWO) {
                    val sqrt = if (isNegative) Complex(0, unaryMinus().sqrt()) else sqrt()
                    return sqrt.pow(other.numerator)
                }

                // Value 10 is arbitrary but unsure what else to use
                if (!isNegative && !other.isNegative) {
                    if(other.numerator < Integer(10) && other.denominator < Integer(10))
                        return root(other.denominator.intValue()).pow(other.numerator)
                }

                return pow(other.toPrecision(precision))
            }
            is RealDouble -> return RealDouble(toDouble().pow(other.value))
            is RealBigDec -> {
                if (this.isNegative)
                    return Exp(Times(other, this.unaryMinus().log() + Times(I(), Pi()))).eval() as NumberExpr

                return this.pow(other)
            }
            is Complex -> throw UnsupportedOperationException()
        }
    }

    override fun quotient(other: NumberExpr): NumberExpr {
        return when (other) {
            is Complex -> (this / other).round()
            else -> (this / other).floor()
        }
    }

    // TODO in most all cases we want to store a higher precision than is actually used, so reconsider what this does
    override fun toPrecision(precision: Int): NumberExpr {
        if (precision == MachinePrecision)
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

    fun exp() = RealBigDec(value.exp(maxStoredPrecision), precision)
    fun log() = RealBigDec(value.log(maxStoredPrecision), precision)
    fun sqrt() = RealBigDec(if (isZero) BigDecimal.ZERO else value.sqrt(maxStoredPrecision), precision)

    fun root(n: Int): RealBigDec {
        return when {
            isZero -> RealBigDec(BigDecimal.ZERO, precision)
            n == 1 -> this
            n == 2 -> sqrt()
            else -> RealBigDec(value.root(n, maxStoredPrecision), precision)
        }
    }
}
