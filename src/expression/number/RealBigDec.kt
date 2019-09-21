package org.cerion.symcalc.expression.number

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.exception.OperationException
import org.cerion.symcalc.expression.AtomExpr
import org.cerion.symcalc.expression.function.core.N
import org.nevec.rjm.BigDecimalMath
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min

class RealBigDec(override val value: BigDecimal, override val precision: Int) : NumberExpr(), AtomExpr {

    companion object {
        val ZERO = RealBigDec(BigDecimal("0.0"))
        private const val PrecisionMin = 20
        private const val PrecisionStep = 10

        fun getStoredPrecision(desired: Int): Int {
            if(desired < PrecisionMin - PrecisionStep)
                return PrecisionMin

            var result = PrecisionMin
            while (desired > result - PrecisionStep)
                result += PrecisionStep

            return result
        }
    }

    constructor(value: BigDecimal) : this(value, value.precision())
    constructor(value: String) : this(BigDecimal(value))
    constructor(value: String, precision: Int) : this(BigDecimal(value), precision)

    override val type: ExprType get() = ExprType.NUMBER
    override val numType: NumberType get() = NumberType.REAL_BIGDEC
    override val isZero: Boolean get() = value.compareTo(BigDecimal.ZERO) == 0
    override val isOne: Boolean get() = value == BigDecimal.ONE
    override val isNegative: Boolean get() = value.signum() == -1
    val accuracy: Int get() = value.scale()

    fun toDouble(): Double = value.toDouble()

    private fun getRepresentedValue(): BigDecimal {
        if (precision < value.precision())
            return value.round(MathContext(precision, RoundingMode.HALF_UP))
        else if (precision > value.precision())
            return value.setScale(precision - (value.precision() - value.scale()), RoundingMode.HALF_UP)

        return value
    }

    override fun toString(): String = "${getRepresentedValue()}`$precision"
    override fun unaryMinus(): RealBigDec = RealBigDec(value.negate(), precision)

    override fun compareTo(other: NumberExpr): Int {
        if (other is RealBigDec)
            return getRepresentedValue().compareTo(other.getRepresentedValue())

        if (other is RealDouble)
            return value.toDouble().compareTo(other.value)

        val real = other.evaluate(precision) as RealBigDec
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

    override fun div(other: NumberExpr): NumberExpr {
        when (other) {
            is Integer -> {
                val n = BigDecimal(other.value)
                return RealBigDec(value.divide(n, MathContext(precision, RoundingMode.HALF_UP)))
            }

            is Rational -> return (this * other.denominator) / other.numerator
            is RealDouble -> return RealDouble(toDouble() / other.value)
            is RealBigDec -> {
                val mc = MathContext(precision, RoundingMode.HALF_UP)
                val result = RealBigDec( this.value.divide(other.value, mc))

                return evaluatePrecision(result, this, other)
            }

            is Complex -> return Complex(this) / other
            else -> throw NotImplementedError()
        }
    }

    fun pow(other: RealBigDec): NumberExpr {
        // TODO verify integer power does not get to this function via both NumberExpr power and Power() function
        if (isNegative)
            throw OperationException("lhs cannot be negative")

        if (isZero)
            return ZERO

        val p = min(precision, other.precision)
        val mc = MathContext(getStoredPrecision(p), RoundingMode.HALF_UP)
        // TODO this is still not correct because of the accuracy thing
        val x = value.setScale(accuracy + 2).round(mc) // Add 2 extra digits so scale/precision is correct

        val logx = BigDecimalMath.log(x)
        val ylogx = other.value.multiply(logx, mc)

        return RealBigDec(ylogx, p).exp()
    }

    override fun evaluate(precision: Int): NumberExpr {
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

    fun exp(): RealBigDec {
        val storedPrecision = getStoredPrecision(precision)
        val mc = MathContext(storedPrecision, RoundingMode.HALF_UP)

        if (isNegative) {
            val denominator = this.unaryMinus().exp()
            return (Integer.ONE.evaluate(precision) / denominator) as RealBigDec
        }

        // Maclaurin series 1 + x + x^2/2! + x^3/3! + ...
        var result = value.plus(BigDecimal.ONE)
        var factorial = BigDecimal.ONE
        var power = value

        for(i in 2..1000) {
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

    private fun evaluatePrecision(result: RealBigDec, a: RealBigDec, b: RealBigDec): RealBigDec {
        val min = min(a.value.precision(), b.value.precision())
        if (min == 0 && a.isZero || b.isZero)
            return result

        var ret = RealBigDec(result.value.round(MathContext(min, RoundingMode.HALF_UP)))
        if (result.precision < min)
            ret = RealBigDec(result.value.setScale(min-1, RoundingMode.HALF_UP))

        return ret
    }
}
