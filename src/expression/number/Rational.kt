package org.cerion.symcalc.expression.number

import org.cerion.symcalc.expression.function.arithmetic.Divide

class Rational constructor(val numerator: Integer, val denominator: Integer = Integer.ONE) : NumberExpr() {

    override val type: ExprType get() = ExprType.NUMBER
    override val isZero: Boolean get() = numerator.isZero
    override val isOne: Boolean get() = numerator.equals(denominator)
    override val numType: NumberType get() = NumberType.RATIONAL
    override val isNegative: Boolean get() = numerator.isNegative
    override val precision: Int get() = InfinitePrecision

    constructor(n: Int, d: Int) : this(Integer(n.toLong()), Integer(d.toLong()))

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
        return if (d.isOne) n else Rational(n, d)
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

    override fun compareTo(other: NumberExpr): Int {
        if (other is Complex)
            return Complex(this).compareTo(other)

        if (other.precision in MachinePrecision until InfinitePrecision)
            return toPrecision(other.precision).compareTo(other)

        return toPrecision(MachinePrecision).compareTo(other)
    }

    companion object {
        val ZERO = Rational(Integer.ZERO, Integer.ONE)
        val ONE = Rational(Integer.ONE, Integer.ONE)
        val HALF = Rational(Integer.ONE, Integer.TWO)
        val THIRD = Rational(Integer.ONE, Integer(3))
    }
}