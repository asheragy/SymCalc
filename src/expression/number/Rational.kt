package org.cerion.symcalc.expression.number

import java.math.RoundingMode

class Rational constructor(n: Integer, d: Integer = Integer.ONE) : NumberExpr(n, d) {

    override val value: Any? get() = null
    override val isZero: Boolean get() = numerator.isZero
    override val isOne: Boolean get() = numerator.equals(denominator)
    override val numType: NumberType get() = NumberType.RATIONAL
    override val isNegative: Boolean get() = numerator.isNegative
    override val precision: Int get() = InfinitePrecision

    val numerator: Integer get() = args[0] as Integer
    val denominator: Integer get() = args[1] as Integer

    constructor(n: Int, d: Int) : this(Integer(n.toLong()), Integer(d.toLong()))

    override fun evaluate(): NumberExpr {
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
    
    override fun evaluate(precision: Int): NumberExpr {
        return when (precision) {
            InfinitePrecision -> this
            MachinePrecision -> RealDouble(numerator.value.toDouble() / denominator.value.toDouble())
            else -> {
                val a = numerator.toBigDecimal()
                val b = denominator.toBigDecimal()
                val t = a.divide(b, precision, RoundingMode.HALF_UP)
                RealBigDec(t)
            }
        }
    }
    
    override fun toString(): String = "$numerator/$denominator"
    override fun unaryMinus(): NumberExpr = Rational(numerator.unaryMinus(), denominator)

    fun reciprocal(): NumberExpr = Rational(denominator, numerator).evaluate()

    override fun plus(other: NumberExpr): NumberExpr {
        val result: Rational
        when (other.numType) {
            NumberType.INTEGER -> {
                val norm = other.asInteger() * denominator
                return Rational(numerator.plus(norm), denominator)
            }

            NumberType.RATIONAL -> {
                val div = other as Rational
                val n1 = div.numerator * denominator
                val n2 = numerator * div.denominator

                result = Rational(n1 + n2, denominator * div.denominator)
                return result.evaluate()
            }
            else -> return other + this
        }
    }

    override fun times(other: NumberExpr): NumberExpr {
        val result: Rational

        when (other.numType) {
            NumberType.INTEGER -> {
                result = Rational(numerator * other.asInteger(), denominator)
                return result.evaluate()
            }
            NumberType.RATIONAL -> {
                val t = other as Rational
                result = Rational(numerator * t.numerator, denominator * t.denominator)
                return result.evaluate()
            }
            else -> return other * this
        }
    }

    override fun div(other: NumberExpr): NumberExpr {
        val result: Rational
        when (other.numType) {
            NumberType.INTEGER -> {
                result = Rational(numerator, denominator * other.asInteger())
                return result.evaluate()
            }
            NumberType.RATIONAL -> {
                val t = other as Rational
                result = Rational(numerator * t.denominator, denominator * t.numerator)
                return result.evaluate()
            }
            else -> return (Integer.ONE / other) * this
        }
    }

    override fun compareTo(other: NumberExpr): Int {
        if (other is Complex)
            return Complex(this).compareTo(other)

        return evaluate(MachinePrecision).compareTo(other)
    }

    companion object {
        val ZERO = Rational(Integer.ZERO, Integer.ONE)
        val ONE = Rational(Integer.ONE, Integer.ONE)
        val HALF = Rational(Integer.ONE, Integer.TWO)
        val THIRD = Rational(Integer.ONE, Integer(3))
    }
}