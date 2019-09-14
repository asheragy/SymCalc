package org.cerion.symcalc.expression.number

import org.cerion.symcalc.exception.IterationLimitExceeded
import org.cerion.symcalc.expression.function.core.N
import org.nevec.rjm.BigDecimalMath
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.min

class RealBigDec(override val value: BigDecimal) : NumberExpr() {

    companion object {
        val ZERO = RealBigDec(BigDecimal("0.0"))
    }

    constructor(value: String) : this(BigDecimal(value))

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

        return evaluate(MachinePrecision).compareTo(other.evaluate(MachinePrecision))
    }

    override fun plus(other: NumberExpr): NumberExpr {
        when (other) {
            is Integer -> {
                val bigDec = other.toBigDecimal()
                return RealBigDec( this.value.plus(bigDec))
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
                val a = this.evaluate(min) as RealBigDec
                val b = other.evaluate(min) as RealBigDec

                return RealBigDec( a.value.plus(b.value))
            }
            is Complex -> {
                return other + this
            }
            else -> throw NotImplementedError()
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        when(other) {
            is Integer -> return RealBigDec(value.times(other.value.toBigDecimal()))
            is Rational -> return (this * other.numerator) / other.denominator
            is RealDouble -> return other * this
            is RealBigDec -> {
                // Both are BigDecimal
                val bd = this.value.times(other.value)
                val result = RealBigDec(bd)
                return evaluatePrecision(result, this, other)
            }

            is Complex -> return Complex(this * other.real, this * other.img)
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
        val bigDec = BigDecimalMath.pow(value, other.value)
        return RealBigDec(bigDec)
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
        val mc = MathContext(precision + 2, RoundingMode.HALF_UP)

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
                return RealBigDec(result.round(MathContext(precision, RoundingMode.HALF_UP)))

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
