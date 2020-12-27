package org.cerion.symcalc.number

import org.cerion.symcalc.function.arithmetic.Divide

class Rational private constructor(val numerator: Integer, val denominator: Integer = Integer.ONE, val reduced: Boolean = false) : NumberExpr() {

    companion object {
        val ZERO = Rational(Integer.ZERO, Integer.ONE)
        val ONE = Rational(Integer.ONE, Integer.ONE)
        val HALF = Rational(Integer.ONE, Integer.TWO)
        val THIRD = Rational(Integer.ONE, Integer(3))

        private fun convertArg(n: Any): Integer {
            return when(n) {
                is Integer -> n
                is Int -> Integer(n)
                is String -> Integer(n)
                else -> throw IllegalArgumentException("cannot convert $n to Integer")
            }
        }
    }

    override val type: ExprType get() = ExprType.NUMBER
    override val isZero: Boolean get() = numerator.isZero
    override val isOne: Boolean get() = numerator.equals(denominator)
    override val numType: NumberType get() = NumberType.RATIONAL
    override val isNegative: Boolean get() = numerator.isNegative
    override val precision: Int get() = InfinitePrecision

    constructor(n: Any, d: Any = Integer.ONE) : this(convertArg(n), convertArg(d))

    override fun eval(): NumberExpr {
        //Reduce with GCD
        val gcd = numerator.gcd(denominator)
        var n: Integer = numerator
        var d: Integer = denominator

        if (!gcd.isOne) {
            n = n.div(gcd).asInteger()
            d = d.div(gcd).asInteger()
        }

        //Never allow bottom to be negative
        if (d.signum == -1) {
            n = n.unaryMinus()
            d = d.unaryMinus()
        }

        //Integer since denominator is one
        return if (d.isOne) n else Rational(n, d, true)
    }
    
    override fun toPrecision(precision: Int): NumberExpr {
        return when (precision) {
            InfinitePrecision -> this
            MachinePrecision -> RealDouble(numerator.value.toDouble() / denominator.value.toDouble())
            else -> {
                //val a = numerator.toBigDecimal()
                //val b = denominator.toBigDecimal()
                //val t = a.divide(b, precision, RoundingMode.HALF_UP)
                //RealBigDec(t)
                return RealBigDec(numerator.toBigDecimal(), precision) / RealBigDec(denominator.toBigDecimal(), precision)
            }
        }
    }
    
    override fun toString(): String = "$numerator/$denominator"
    override fun unaryMinus(): NumberExpr = Rational(numerator.unaryMinus(), denominator)

    fun reciprocal(): NumberExpr = Rational(denominator, numerator).eval()

    override fun plus(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer -> {
                val norm = other * denominator
                Rational(numerator.plus(norm), denominator).eval()
            }
            is Rational -> {
                val n1 = other.numerator * denominator
                val n2 = numerator * other.denominator
                Rational(n1 + n2, denominator * other.denominator).eval()
            }
            else -> other + this
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer -> Rational(numerator * other.asInteger(), denominator).eval()
            is Rational -> Rational(numerator * other.numerator, denominator * other.denominator).eval()
            else -> other * this
        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer -> Rational(numerator, denominator * other).eval()
            is Rational -> Rational(numerator * other.denominator, denominator * other.numerator).eval()
            else -> (Integer.ONE / other) * this
        }
    }

    override fun pow(other: NumberExpr): NumberExpr {
        return when (other) {
            is Integer -> {
                val top = numerator.pow(other) // These could be rational on negative integer
                val bottom = denominator.pow(other)
                Divide(top, bottom).eval() as NumberExpr
            }
            is RealDouble -> this.toPrecision(other.precision).pow(other)
            is RealBigDec -> this.toPrecision(other.precision).pow(other)
            else -> throw UnsupportedOperationException()
        }
    }

    override fun quotient(other: NumberExpr): NumberExpr {
        return when (other) {
            is Complex -> (this / other).round()
            else -> (this / other).floor()
        }
    }

    override fun compareTo(other: NumberExpr): Int {
        if (!reduced)
            return eval().compareTo(other)

        if (other is Rational) {
            if (!other.reduced)
                return compareTo(other.eval())

            if (numerator == other.numerator && denominator == other.denominator)
                return 0

            val dr1 = numerator.value.divideAndRemainder(denominator.value)
            val dr2 = other.numerator.value.divideAndRemainder(other.denominator.value)

            // First compare quotients
            return when (val x1 = dr1[0].compareTo(dr2[0])) {
                0 -> {
                    // Then remainders
                    when (val x2 = dr1[1].compareTo(dr2[1])) {
                        // If same quotient/remainder then largest is whatever has smaller denominator
                        0 -> other.denominator.compareTo(denominator)
                        else -> x2
                    }
                }
                else -> x1
            }
        }

        if (other is Complex)
            return Complex(this).compareTo(other)

        if (other.precision in MachinePrecision until InfinitePrecision)
            return toPrecision(other.precision).compareTo(other)

        return toPrecision(MachinePrecision).compareTo(other)
    }

    override fun floor(): NumberExpr {
        val dr = numerator.value.divideAndRemainder(denominator.value)
        val n = dr[0]
        val d = dr[1]

        if (d.signum() < 0)
            return Integer(n) - Integer.ONE

        return Integer(n)
    }

    override fun round(): NumberExpr {
        val dr = numerator.value.divideAndRemainder(denominator.value)
        val n = dr[0]
        val d = dr[1]

        val remainder = d.abs().toDouble() / denominator.value.toDouble()
        if (remainder >= 0.5) {
            return if (isNegative)
                Integer(n) - Integer.ONE
            else
                Integer(n) + Integer.ONE
        }

        return Integer(n)
    }
}