package org.cerion.symcalc.number

import org.cerion.symcalc.constant.I
import org.cerion.symcalc.constant.Pi
import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.exception.OperationException
import org.cerion.symcalc.expression.AtomExpr
import org.cerion.symcalc.function.arithmetic.Exp
import org.cerion.symcalc.function.arithmetic.Times
import org.cerion.symcalc.function.core.N
import org.nevec.rjm.BigDecimalMath
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

class RealBigDec(override val value: BigDecimal, override val precision: Int) : NumberExpr(), AtomExpr {

    companion object {
        val ZERO = RealBigDec(BigDecimal("0.0"))
        private const val PrecisionMin = 20
        private const val PrecisionStep = 10

        // TODO remove pattern of calling this followed by setScale on external usages of BigDecimal
        fun getStoredPrecision(desired: Int): Int {
            if(desired < PrecisionMin - PrecisionStep)
                return PrecisionMin

            var result = PrecisionMin
            while (desired > result - PrecisionStep)
                result += PrecisionStep

            return result
        }
    }

    private val storedPrecision
        get() = getStoredPrecision(precision)

    // TODO check implications of only storing the scaled value vs getting it on demand
    val valueScaled: BigDecimal
        get() = value.setScale(getStoredPrecision(precision))

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
        return forcePrecision(precision)
    }

    fun forcePrecision(newPrecision: Int): BigDecimal {
        if (newPrecision < value.precision())
            return value.round(MathContext(newPrecision, RoundingMode.HALF_UP))
        else if (newPrecision > value.precision())
            return value.setScale(newPrecision - (value.precision() - value.scale()), RoundingMode.HALF_UP)

        return value
    }

    fun increasePrecision(newPrecision: Int): RealBigDec = RealBigDec(this.forcePrecision(newPrecision))

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
                // TODO_LP issue with zero and precision, this is just a workaround until more is learned on how it
                if (isZero)
                    return other
                if (other.isZero)
                    return this

                val min = min(this.precision, other.precision)
                return RealBigDec(value.add(other.value), min)
            }
            is Complex -> {
                return other + this
            }
            else -> throw NotImplementedError()
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
            else -> throw NotImplementedError()
        }
    }

    operator fun div(other: RealBigDec): RealBigDec {
        val newPrecision = min(precision, other.precision)
        val mc = MathContext(getStoredPrecision(newPrecision), RoundingMode.HALF_UP)
        return RealBigDec( this.value.divide(other.value, mc), newPrecision)
    }

    override fun div(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer -> RealBigDec(value.divide(BigDecimal(other.value), MathContext(getStoredPrecision(precision), RoundingMode.HALF_UP)), precision)
            is Rational -> (this * other.denominator) / other.numerator
            is RealDouble -> RealDouble(toDouble() / other.value)
            is RealBigDec -> this / other
            is Complex -> Complex(this) / other
            else -> throw NotImplementedError()
        }
    }

    fun pow(other: RealBigDec): RealBigDec {
        if (isNegative)
            throw OperationException("lhs cannot be negative")

        if (isZero)
            return this

        val p = min(precision, other.precision)
        val mc = MathContext(getStoredPrecision(p), RoundingMode.HALF_UP)

        var x = value
        // TODO should be adding more than 2 digits and it should be handled elsewhere
        if (x.precision() < mc.precision)
            x = value.setScale(mc.precision - value.precision() + value.scale()).round(mc) // Add 2 extra digits so scale/precision is correct

        val ylogx = other.value.multiply(x.log(), mc)

        return RealBigDec(ylogx, p).exp()
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
                if (other == Rational.HALF)
                    return if (isNegative) Complex(0, unaryMinus().sqrt()) else sqrt()

                // TODO negative may work here but check cases
                // TODO optimal values here
                if (!isNegative && !other.isNegative) {
                    if (other.denominator == Integer(2))
                        return sqrt().pow(other.numerator)
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
            else -> throw UnsupportedOperationException()
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

    // TODO move to extensions
    fun exp(): RealBigDec {
        if (precision < 200) {
            val t = forcePrecision(getStoredPrecision(precision))
            return RealBigDec(BigDecimalMath.exp(t), precision)
        }

        val storedPrecision = getStoredPrecision(precision)
        val mc = MathContext(storedPrecision, RoundingMode.HALF_UP)

        if (isNegative) {
            val denominator = this.unaryMinus().exp()
            return (Integer.ONE.toPrecision(precision) / denominator) as RealBigDec
        }

        // Maclaurin series 1 + x + x^2/2! + x^3/3! + ...
        var result = value.plus(BigDecimal.ONE)
        var factorial = BigDecimal.ONE
        var power = value

        for(i in 2..10000) {
            power = power.multiply(value, mc)
            factorial = factorial.multiply(BigDecimal(i))
            var term = power.divide(factorial, mc)
            term = result.add(term, mc)

            if (result == term)
                return RealBigDec(result, precision)

            result = term
        }

        throw IterationLimitExceeded()
    }

    fun log(): RealBigDec = RealBigDec(value.log(storedPrecision), precision)
    fun sqrt(): RealBigDec = if (isZero) RealBigDec(BigDecimal.ZERO, precision) else RealBigDec(value.sqrt(storedPrecision), precision)

    fun root(n: Int): RealBigDec {
        return when {
            isZero -> RealBigDec(BigDecimal.ZERO, precision)
            n == 1 -> this
            n == 2 -> sqrt()
            else -> RealBigDec(value.root(n, storedPrecision), precision)
        }
    }
}
