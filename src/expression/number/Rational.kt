package org.cerion.symcalc.expression.number

import java.math.RoundingMode

class Rational constructor(n: IntegerNum, d: IntegerNum = IntegerNum.ONE) : NumberExpr() {

    override val value: Any? get() = null
    override val isZero: Boolean get() = numerator.isZero
    override val isOne: Boolean get() = numerator.equals(denominator)
    override val numType: NumberType get() = NumberType.RATIONAL
    override val isNegative: Boolean get() = numerator.isNegative
    override val precision: Int get() = InfinitePrecision

    val numerator: IntegerNum get() = args[0] as IntegerNum
    val denominator: IntegerNum get() = args[1] as IntegerNum

    init {
        setArg(0, n)
        setArg(1, d)
    }

    constructor(n: Int, d: Int) : this(IntegerNum(n.toLong()), IntegerNum(d.toLong()))

    override fun evaluate(): NumberExpr {
        //Reduce with GCD
        val gcd = numerator.gcd(denominator)
        var n: IntegerNum = numerator
        var d: IntegerNum = denominator

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
            SYSTEM_DECIMAL_PRECISION -> RealNum_Double(numerator.toDouble() / denominator.toDouble())
            else -> {
                val a = numerator.toBigDecimal()
                val b = denominator.toBigDecimal()
                val t = a.divide(b, precision, RoundingMode.HALF_UP)
                RealNum_BigDecimal(t)
            }
        }
    }
    
    override fun toString(): String = "$numerator/$denominator"
    override fun toDouble(): Double = numerator.toBigDecimal().toDouble() / denominator.toBigDecimal().toDouble()
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
            else -> return other * this
        }
    }

    override fun compareTo(other: NumberExpr): Int {
        return toDouble().compareTo(other.toDouble())
    }

    companion object {
        val ZERO = Rational(IntegerNum.ZERO, IntegerNum.ONE)
        val ONE = Rational(IntegerNum.ONE, IntegerNum.ONE)
        val HALF = Rational(IntegerNum.ONE, IntegerNum.TWO)
    }
}